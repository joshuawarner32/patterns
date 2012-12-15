package pattern.graph;

import java.util.Arrays;

import pattern.Value;
import pattern.Variable;
import pattern.Node;

class ListTransition extends Transition {
  Variable[] variables;

  public ListTransition(Variable var, Variable[] variables) {
    super(var);
    this.variables = variables;
  }

  int arity() {
    return variables.length;
  }

  void forward(Context ctx) {
    Expr e = ctx.get(var);
    if(e.isAtomic() || e.size() != variables.length) {
      throw new IllegalStateException();
    }
    ctx.unbind(var);

    for(int i = 0; i < variables.length; i++) {
      ctx.bind(variables[i], e.get(i));
    }
  }

  void reverse(Context ctx) {
    throw new RuntimeException();
    // Value[] values = new Value[variables.length];
    // for(int i = 0; i < variables.length; i++) {
    //   values[i] = ctx.get(variables[i]);
    //   ctx.unbind(variables[i]);
    // }
    // ctx.bind(var, new Node(values));
  }

  public boolean equals(Object o) {
    if(!(o instanceof ListTransition)) {
      return false;
    }
    ListTransition l = (ListTransition)o;
    return var == l.var && Arrays.equals(variables, l.variables);
  }

  // public int hashCode() {
  //   return var.hashCode() + 3 * symbol.hashCode();
  // }
}