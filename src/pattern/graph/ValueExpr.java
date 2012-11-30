package pattern.graph;

import pattern.Variable;
import pattern.Value;

class ValueExpr extends Expr {

  final Value value;

  ValueExpr(Value value) {
    this.value = value;
  }

  public ContextExpr toContextExpr(State rootState, Variable rootVariable) {
    return new ContextExpr(rootState, value, rootVariable);

  }

  public Value toValue() {
    return value;
  }

}