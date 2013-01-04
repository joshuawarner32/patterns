package pattern.reducer.graph;

import java.util.Map;
import java.util.HashMap;

import pattern.Variable;
import pattern.Value;

import pattern.Binding;

class Context implements Binding {

  private State rootState;
  private Variable rootVariable;

  private GraphReducer reducer;

  private State state;
  private Map<Variable, Expr> bindings = new HashMap<Variable, Expr>();

  public Context(GraphReducer reducer, State state, Variable variable, Value value) {
    this.reducer = reducer;
    this.rootState = state;
    this.rootVariable = variable;
    this.state = state;
    bind(variable, new Expr(value));
  }
  
  Expr getExpr(Variable var) {
    Expr ret = bindings.get(var);
    if(ret == null) {
      throw new IllegalStateException();
    }
    return ret;
  }

  public Value get(Variable var) {
    return getExpr(var).toValue();
  }

  public Value getRawValue() {
    return getExpr(rootVariable).toRawValue();
  }

  public void bind(Variable var, Expr expr) {
    if(bindings.put(var, expr) != null) {
      throw new IllegalStateException();
    }
  }

  void unbind(Variable var) {
    if(bindings.remove(var) == null) {
      throw new IllegalStateException();
    }
  }

  Context newChild(Value value) {
    return new Context(reducer, rootState, rootVariable, value);
  }

  private boolean transition() {
    // for(Map.Entry<Variable, Expr> b : bindings.entrySet()) {
    //   Variable var = b.getKey();
    //   Expr expr = b.getValue();

    //   TransitionsForVariable ts = state.transitionsForVariable(var);

    //   if(ts != null) {
    //     expr.prepare(this);
    //     State s = expr.partialReduce(this, ts);
    //     if(s != state) {
    //       state = s;
    //       return true;
    //     }
    //   }
    // }
    return false;
  }

  private boolean step() {
    while(transition()) {}
    State ns = reducer.reducedState(state);
    State old = state;
    state = ns;
    return ns != old;
  }

  Value result() {
    while(step()) {}
    while(state != rootState) {
      // TODO: walk up transition tree
      throw new UnsupportedOperationException();
    }
    return get(rootVariable);
  }
}