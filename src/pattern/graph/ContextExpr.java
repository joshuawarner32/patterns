package pattern.graph;

import pattern.Variable;
import pattern.Value;

class ContextExpr extends Expr {
  final Context context;
  final Variable variable;

  ContextExpr(State state, Variable variable, Value value) {
    context = new Context(state, variable, value);
    this.variable = variable;
  }

  ContextExpr(Context context, Variable variable) {
    this.context = context;
    this.variable = variable;
  }

  public ContextExpr toContextExpr() {
    return this;
  }

  public Value toValue() {
    return context.get(variable);
  }

  public boolean isReducableTo(State state) {

  }
}