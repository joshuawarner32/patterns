
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Graph {

  private Atom valAtom = new Atom("_val");
  private State root = new State(new Pattern(valAtom));

  private class Context implements Binding {
    Map<Atom, Expr> top = new HashMap<Atom, Expr>();

    void bind(Atom var, Expr val) {
      top.put(var, val);
    }

    Expr getExpr(Atom var) {
      return top.get(var);
    }

    public Value get(Atom var) {
      return getExpr(var).toValue(this);
    }

    void step(StateExpr expr) {
      expr.bindWith(this);
      State s = expr.state;
      State s2;
      while((s2 = s.maybeTransition(this)) != s) {
        s = s2;
      }
      s.applyTo(this);
    }

    State buildState() {
      System.out.println("buildState " + top);
      for(Map.Entry<Atom, Expr> b : top.entrySet()) {
        Expr e = b.getValue();
        if(e instanceof ConstExpr) {
          ConstExpr ce = (ConstExpr)e;
          if(ce.val instanceof Atom) {
            // we're where we want to be
            System.out.println("we're done");
          } else {
            State s = root;
            Pattern p = new Pattern(ce.val);
            s.transitions.add(new Transition(b.getKey(), p));
            System.out.println("work to do");
          }
        } else if(e instanceof StateExpr) {
          StateExpr se = (StateExpr)e;

          System.out.println("state expr");
        }
      }
      return null;
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

  }

  private static class StateExpr implements Expr {
    State state;
    Map<Atom, Expr> bindings = new HashMap<Atom, Expr>();

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
    Atom var;
    Pattern pat;

    Transition(Atom var, Pattern pat) {
      this.var = var;
      this.pat = pat;
    }
  }

  private static class State {
    Pattern pattern;
    Expr reduction;
    List<Transition> transitions = new ArrayList<Transition>();

    State(Pattern pattern) {
      this.pattern = pattern;
    }

    State maybeTransition(Context ctx) {
      for(Transition t : transitions) {
        System.out.println("trying transition");
      }
      return this;
    }

    public void applyTo(Context ctx) {
      
    }
  }

  private State place(Pattern p) {
    Context ctx = new Context();
    StateExpr expr = new StateExpr(root, valAtom, new ConstExpr(p.getNakedValue()));
    ctx.step(expr);
    return ctx.buildState();
  }

  public void putReduction(Pattern match, Pattern replace) {
    place(match);
    place(replace);
  }

  public Value reduce(Value value) {
    System.out.println("reduce");
    Context ctx = new Context();
    StateExpr expr = new StateExpr(root, valAtom, new ConstExpr(value));
    ctx.step(expr);
    return ctx.get(valAtom);
  }

}