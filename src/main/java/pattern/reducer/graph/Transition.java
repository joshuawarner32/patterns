package pattern.graph;

import pattern.Variable;

abstract class Transition implements TransitionOrReduction {

  final Variable var;

  Transition(Variable var) {
    this.var = var;
  }
  
  abstract void forward(Context ctx);
  abstract void reverse(Context ctx);

}