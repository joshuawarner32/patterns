package pattern.graph;

import pattern.Value;
import pattern.Variable;

public class Graph {

  private State rootState = new State();
  private Variable rootVariable = new Variable("_");

  public Value reduce(Value value) {
    Context ctx = new Context(rootState);
    ctx.bind(rootVariable, value);
    return ctx.result(rootVariable);
  }

}