package pattern;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Graph {

  private Variable valVariable = new Variable("_val");
  private State root = new State(new Pattern(new Variable[] {valVariable}, valVariable));

  private class Context implements Binding {
    Map<Variable, Expr> top = new HashMap<Variable, Expr>();

    State state = root;

    void bind(Variable var, Expr val) {
      top.put(var, val);
    }

    void unbind(Variable var) {
      top.remove(var);
    }

    Expr getExpr(Variable var) {
      return top.get(var);
    }

    public Value get(Variable var) {
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
        Map<Variable, Expr> repl = new HashMap<Variable, Expr>();
        for(Map.Entry<Variable, Expr> b : top.entrySet()) {
          Variable var = b.getKey();
          Expr e = b.getValue();

          if(e instanceof ConstExpr) {
            ConstExpr ce = (ConstExpr)e;
            if(ce.val instanceof Variable) {
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
              Variable[] Variables = new Variable[n.size()];
              for(int i = 0; i < n.size(); i++) {
                Value v = n.get(i);
                if(v instanceof Variable) {
                  Variables[i] = (Variable)v;
                } else {
                  Variables[i] = new Variable("$");
                }
                repl.put(Variables[i], new ConstExpr(v));
              }
              s.getTransition(var).putListTranslation(Variables, state = new State(new Pattern(ce.val)));

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
    Map<Variable, Expr> bindings = new HashMap<Variable, Expr>();

    StateExpr(State state) {
      this.state = state;
    }

    StateExpr(State state, Variable a, Expr e) {
      this.state = state;
      bindings.put(a, e);
    }

    public Value toValue(Context ctx) {
      return state.pattern.replace(ctx);
    }

    public void bindWith(Context ctx, Variable a, StateExpr parent) {
      bindWith(ctx);
    }
    
    public void bindWith(Context ctx) {
      for(Map.Entry<Variable, Expr> b : bindings.entrySet()) {
        b.getValue().bindWith(ctx);
        ctx.bind(b.getKey(), b.getValue());
      }
    }
  }

  private static class ReverseTransition {
    State state;

    ReverseTransition(State state) {
      this.state = state;
    }
  }

  private static class ReverseSymbolTransition extends ReverseTransition {
    Variable define;
    Symbol value;

    ReverseSymbolTransition(State state, Variable define, Symbol value) {
      super(state);
      this.define = define;
      this.value = value;
    }
  }

  private static class ReverseListTransition extends ReverseTransition {
    Variable define;
    Variable[] variables;

    ReverseListTransition(State state, Variable define, Variable[] variables) {
      super(state);
      this.define = define;
      this.variables = variables;
    }
  }

  private static class Transition {
    private static class ListState {
      Variable[] Variables;
      State state;

      ListState(Variable[] Variables, State state) {
        this.Variables = Variables;
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

    void putListTranslation(Variable[] Variables, State s) {
      listStates.put(Variables.length, new ListState(Variables, s));
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
            ctx.bind(s.Variables[i], new ConstExpr(v.get(i)));
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
    List<ReverseTransition> reverseTransitions = new ArrayList<ReverseTransition>();
    Map<Variable, Transition> transitions = new HashMap<Variable, Transition>();

    State(Pattern pattern) {
      this.pattern = pattern;
    }

    Transition getTransition(Variable a) {
      Transition t = transitions.get(a);
      if(t == null) {
        transitions.put(a, t = new Transition());
      }
      return t;
    }

    State maybeTransition(Context ctx) {
      System.out.println("maybeTransition state " + pattern.getNakedValue().toString());
      for(Map.Entry<Variable, Transition> e : transitions.entrySet()) {
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
    ctx.bind(valVariable, new ConstExpr(p.getNakedValue()));
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
    ctx.bind(valVariable, new ConstExpr(value));
    while(ctx.step()) {}
    return ctx.result();
  }

}