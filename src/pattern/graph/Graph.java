package pattern.graph;

import pattern.Value;

public class Graph {

  private State rootState = new State();

  public Value reduce(Value value) {
    Context ctx = new Context(rootState);
    return value;
  }

}