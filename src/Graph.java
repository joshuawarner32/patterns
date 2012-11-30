
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Graph {

  private Atom valAtom = new Atom("_val");
  private State root = new State(new Pattern(new Atom[] {valAtom}, valAtom));

  private class Context implements Binding {
    Map<Atom, Expr> top = new HashMap<Atom, Expr>();

    State state = root;

    void bind(Atom var, Expr val) {
      top.put(var, val);
    }

    void unbind(Atom var) {
      top.remove(var);
    }

    Expr getExpr(Atom var) {
      return top.get(var);
    }

    public Value get(Atom var) {
      return getExpr(var).toValue(this);
    }

    public Value result() {
      System.out.println("result: " + state.pattern.getNakedValue());
      return state.pattern.replace(this);
    }

    boolean step() {
      State s = state;
      State s2;
      while((s2 = s.maybeTransition(this)) != s) {
        s = s2;
        System.out.println("transition to " + s2);
      }
      if(state != s.reduction) {
        s.applyTo(this);
        state = s.reduction;
        return true;
      }
      return false;
    }

    private boolean buildStateStep() {
      if(top.size() == 0) {
        return false;
      } else {
        Map<Atom, Expr> repl = new HashMap<Atom, Expr>();
        for(Map.Entry<Atom, Expr> b : top.entrySet()) {
          Atom var = b.getKey();
          Expr e = b.getValue();

          if(e instanceof ConstExpr) {
            ConstExpr ce = (ConstExpr)e;
            if(ce.val instanceof Atom) {
              System.out.println("we're done");
            } else if(ce.val instanceof Symbol) {
              System.out.println("sym state " + ce.val);
              State s = state;
              Symbol sym = (Symbol)ce.val;
              s.getTransition(var).putSymbolTransition(sym, state = new State(new Pattern(ce.val)));
            } else {
              System.out.println("node state " + ce.val);
              State s = state;
              Node n = (Node)ce.val;
              Atom[] atoms = new Atom[n.size()];
              for(int i = 0; i < n.size(); i++) {
                Value v = n.get(i);
                if(v instanceof Atom) {
                  atoms[i] = (Atom)v;
                } else {
                  atoms[i] = new Atom("$");
                }
                repl.put(atoms[i], new ConstExpr(v));
              }
              s.getTransition(var).putListTranslation(atoms, state = new State(new Pattern(ce.val)));

            }
          } else if(e instanceof StateExpr) {
            StateExpr se = (StateExpr)e;

            System.out.println("state expr");
            throw new UnsupportedOperationException();
          }
        }
        top = repl;
        return true;
      }
    }

    State buildState() {
      System.out.println("buildState " + top);
      while(buildStateStep()) {}
      return state;
    }
  }

  private interface Expr {
    Value toValue(Context ctx);
    void bindWith(Context ctx);
  }

  private static class ConstExpr implements Expr {
    Value val;

    ConstExpr(Value val) {
      this.val = val;
    }

    public Value toValue(Context ctx) {
      return val;
    }
    
    public void bindWith(Context ctx) {}

    public String toString() {
      return val.toString();
    }

  }

  private static class StateExpr implements Expr {
    State state;
    Map<Atom, Expr> bindings = new HashMap<Atom, Expr>();

    StateExpr(State state) {
      this.state = state;
    }

    StateExpr(State state, Atom a, Expr e) {
      this.state = state;
      bindings.put(a, e);
    }

    public Value toValue(Context ctx) {
      return state.pattern.replace(ctx);
    }

    public void bindWith(Context ctx, Atom a, StateExpr parent) {
      bindWith(ctx);
    }
    
    public void bindWith(Context ctx) {
      for(Map.Entry<Atom, Expr> b : bindings.entrySet()) {
        b.getValue().bindWith(ctx);
        ctx.bind(b.getKey(), b.getValue());
      }
    }
  }

  private static class Transition {
    private static class ListState {
      Atom[] atoms;
      State state;

      ListState(Atom[] atoms, State state) {
        this.atoms = atoms;
        this.state = state;
      }

      public String toString() {
        return state.toString();
      }
    }
    Map<Integer, ListState> listStates = new HashMap<Integer, ListState>();
    Map<Symbol, State> symbolStates = new HashMap<Symbol, State>();

    void putSymbolTransition(Symbol sym, State s) {
      symbolStates.put(sym, s);
    }

    void putListTranslation(Atom[] atoms, State s) {
      listStates.put(atoms.length, new ListState(atoms, s));
    }

    State maybeTransition(Value v, Context ctx) {
      System.out.println("maybeTransition transition " + listStates + " " + symbolStates + " : " + v);
      if(v instanceof Symbol) {
        Symbol sym = (Symbol)v;
        State s = symbolStates.get(sym);
        System.out.println("trans-sym " + s);
        return s;
      } else {
        int size = v.size();
        ListState s = listStates.get(size);
        if(s != null) {
          for(int i = 0; i < size; i++) {
            ctx.bind(s.atoms[i], new ConstExpr(v.get(i)));
          }
          return s.state;
        } else {
          return null;
        }
      }
    }
  }

  private static class State {
    boolean live = true;
    Pattern pattern;
    State reduction = this;
    Map<Atom, Transition> transitions = new HashMap<Atom, Transition>();

    State(Pattern pattern) {
      this.pattern = pattern;
    }

    Transition getTransition(Atom a) {
      Transition t = transitions.get(a);
      if(t == null) {
        transitions.put(a, t = new Transition());
      }
      return t;
    }

    State maybeTransition(Context ctx) {
      System.out.println("maybeTransition state " + pattern.getNakedValue().toString());
      for(Map.Entry<Atom, Transition> e : transitions.entrySet()) {
        State s = e.getValue().maybeTransition(ctx.get(e.getKey()), ctx);
        if(s != null && s.live) {
          ctx.unbind(e.getKey());
          return s;
        }
      }
      return this;
    }

    public void applyTo(Context ctx) {
      // TODO
    }

    public void putReduction(State s) {
      reduction = s;
    }
  }

  private State place(Pattern p, Context ctx) {
    ctx.bind(valAtom, new ConstExpr(p.getNakedValue()));
    ctx.step();
    return ctx.buildState();
  }

  public void putReduction(Pattern match, Pattern replace) {
    Context a = new Context();
    Context b = new Context();
    place(match, a).putReduction(place(replace, b));
  }

  public Value reduce(Value value) {
    System.out.println("reduce");
    Context ctx = new Context();
    ctx.bind(valAtom, new ConstExpr(value));
    while(ctx.step()) {}
    return ctx.result();
  }

}