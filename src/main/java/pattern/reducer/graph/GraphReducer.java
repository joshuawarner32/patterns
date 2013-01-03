package pattern.reducer.graph;

import pattern.Value;
import pattern.Variable;
import pattern.Pattern;
import pattern.Node;

import pattern.reducer.Rule;
import pattern.reducer.Reducer;
import pattern.reducer.ReducerBuilder;

public class GraphReducer implements Reducer {

  private PersistentGraph<State, State> transitionGraph;
  private PersistentGraph<State, Reduction> reductionGraph;

  private State rootState;
  private Variable rootVariable;

  public GraphReducer() {
    graph = new PersistentGraph();
    rootState = new State();
    rootVariable = new Variable("_");
  }

  private GraphReducer(Builder b) {
    transitionGraph = b.transitionGraphBuilder.build();
    reductionGraph = b.reductionGraphBuilder.build();
  }

  public static class Builder implements ReducerBuilder {

  private PersistentGraph.Builder<State, State> transitionGraphBuilder;
  private PersistentGraph.Builder<State, Reduction> reductionGraphBuilder;

    private Builder(GraphReducer g) {
      transitionGraphBuilder = g.transitionGraph.builder();
      reductionGraphBuilder = g.reductionGraph.builder();
    }

    public Reducer build() {
      return new GraphReducer(this);
    }

    public void add(Rule rule) {
      // TODO: create GraphReducer where that's the only rule
      // TODO: merge it in
    }
  }

  public Value reduce(Value val) {
    Context ctx = new Context(rootState, rootVariable, val);

    return val;
  }

  public ReducerBuilder builder() {
    return new Builder(graph);
  }

}