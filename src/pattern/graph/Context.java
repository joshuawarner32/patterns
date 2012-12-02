package pattern.graph;

import java.util.Map;
import java.util.HashMap;

import pattern.Variable;
import pattern.Value;

class Context {

  private State rootState;
  private Variable rootVariable;

  private State state;
  private Map<Variable, Expr> bindings = new HashMap<Variable, Expr>();

  public Context(State state, Variable variable, Value value) {
    this.rootState = state;
    this.rootVariable = variable;
    this.state = state;
    bind(variable, new Expr(value));
  }
  
  Expr get(Variable var) {
    Expr ret = bindings.get(var);
    if(ret == null) {
      throw new IllegalStateException();
    }
    return ret;
  }

  void bind(Variable var, Expr expr) {
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
    return new Context(rootState, rootVariable, value);
  }

  private boolean transition() {
    for(Map.Entry<Variable, Expr> b : bindings.entrySet()) {
      Variable var = b.getKey();
      Expr expr = b.getValue();

      TransitionsForVariable ts = state.transitionsForVariable(var);

      if(ts != null) {
        expr.prepare(this);
        State s = expr.partialReduce(this, ts);
        if(s != state) {
          state = s;
          return true;
        }
      }
    }
    return false;
  }

  private boolean step() {
    while(transition()) {}
    State ns = state.reduction();
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
    return get(rootVariable).toValue();
  }
}