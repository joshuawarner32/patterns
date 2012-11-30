package pattern.graph;

import java.util.Map;
import java.util.HashMap;

import pattern.Variable;
import pattern.Value;

class Context {

  private State state;

  private Map<Variable, Expr> bindings = new HashMap<Variable, Expr>();

  public Context(State state, Variable variable, Value value) {
    this.state = state;
    bind(variable, new ValueExpr(value));
  }
  
  Value get(Variable var) {
    Expr ret = bindings.get(var);
    if(ret == null) {
      throw new IllegalStateException();
    }
    return ret.toValue();
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

  private ContextExpr readyForQuestioning(Expr expr, Map.Entry<Variable, Expr> b) {
    ContextExpr ret = expr.toContextExpr();
    if(ret != expr) {
      b.setValue(ret);
    }
    return ret;
  }

  private boolean transition() {
    for(Map.Entry<Variable, Expr> b : bindings.entrySet()) {
      Variable var = b.getKey();
      Expr value = b.getValue();

      TransitionsForVariable ts = state.transitionsForVariable(var);

      if(ts != null) {
        ContextExpr expr = readyForQuestioning(value, b);

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

  Value result(Variable var) {
    while(step()) {}
    while(!bindings.containsKey(var)) {
      // TODO: walk up transition tree
      throw new UnsupportedOperationException();
    }
    return get(var);
  }
}