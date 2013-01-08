package pattern.uniform;

public class ExpressionAutomaton {

  private static class State {
    private Map<Symbol, State> nextStates = new HashMap<Symbol, State>();
    private State nextVariableState;
  }

  private State rootState = new State();

  public static class Builder {

    private Builder(ExpressionAutomaton a) {
      
    }

    public void add(Expression e) {

    }

    public ExpressionAutomaton build();
  }

  public Builder builder() {
    return new Builder(this);
  }

}