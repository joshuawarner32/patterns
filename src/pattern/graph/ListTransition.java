package pattern.graph;

import pattern.Value;
import pattern.Variable;
import pattern.Node;

class ListTransition extends Transition {
  Variable[] variables;

  public ListTransition(Variable var, Variable[] variables) {
    super(var);
    this.variables = variables;
  }

  void forward(Context ctx) {
    Value v = ctx.get(var);
    if(v.isAtomic() || v.size() != variables.length) {
      throw new IllegalStateException();
    }
    ctx.unbind(var);

    for(int i = 0; i < variables.length; i++) {
      ctx.bind(variables[i], v.get(i));
    }


  }

  void reverse(Context ctx) {
    Value[] values = new Value[variables.length];
    for(int i = 0; i < variables.length; i++) {
      values[i] = ctx.get(variables[i]);
      ctx.unbind(variables[i]);
    }
    ctx.bind(var, new Node(values));
  }
}