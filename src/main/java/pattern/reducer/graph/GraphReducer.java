package pattern.reducer.graph;

import java.util.Map;
import java.util.HashMap;

import pattern.Value;
import pattern.Variable;
import pattern.Pattern;
import pattern.Node;

import pattern.reducer.Rule;
import pattern.reducer.Reducer;
import pattern.reducer.ReducerBuilder;

public class GraphReducer implements Reducer {

  private PersistentGraph<State, State> transitionGraph;
  private Map<State, State> reductionGraph;

  private static final Variable rootVariable = new Variable("_");
  private static final State rootState = new State(rootVariable);

  public GraphReducer() {
    transitionGraph = new PersistentGraph<State, State>();
    reductionGraph = new HashMap<State, State>();
  }

  private GraphReducer(Builder b) {
    transitionGraph = b.transitionGraphBuilder.build();
    reductionGraph = new HashMap<State, State>(b.reductionGraphBuilder);
  }

  public static class Builder implements ReducerBuilder {

  private PersistentGraph.Builder<State, State> transitionGraphBuilder;
  private Map<State, State> reductionGraphBuilder;

    private Builder(GraphReducer g) {
      transitionGraphBuilder = g.transitionGraph.builder();
      reductionGraphBuilder = new HashMap<State, State>(g.reductionGraph);
    }

    public Reducer build() {
      return new GraphReducer(this);
    }

    public void add(Rule rule) {

      // State initialState = new State(rule.match);
      // State finalState = new State(rule.replace);


    }
  }

  State reducedState(State s) {
    State n = reductionGraph.get(s);
    if(n == null) {
      return s;
    } {
      return n;
    }
  }

  public Value reduce(Value val) {
    Context ctx = new Context(this, rootState, rootVariable, val);

    return ctx.result();
  }

  public ReducerBuilder builder() {
    return new Builder(this);
  }

}