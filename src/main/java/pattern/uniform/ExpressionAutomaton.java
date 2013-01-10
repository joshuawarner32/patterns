package pattern.uniform;

import java.util.Map;
import java.util.HashMap;

public class ExpressionAutomaton {

  private static class State {
    private final Object token;
    private Map<Symbol, State> nextStates;

    private State(Object token) {
      this.token = token;
      nextStates = new HashMap<Symbol, State>();
    }

    private State(Object token, Map<Symbol, State> nextStates) {
      this.token = token;
      this.nextStates = new HashMap<Symbol, State>(nextStates);
    }

    private State stateFor(Object token, Symbol sym) {
      State s = nextStates.get(sym);
      if(s == null) {
        nextStates.put(sym, s = new State(token));
      }
      return s;
    }

    private State addExpr(Object token, Symbol[] syms, int index) {
      if(index == syms.length) {
        return this;
      }

      State s;
      if(token == this.token) {
        s = this;
      } else {
        s = new State(token, nextStates);
      }

      State next = s.stateFor(token, syms[index]).addExpr(token, syms, index + 1);
      s.nextStates.put(syms[index], next);

      return s;
    }
  }

  private State rootState = new State(new Object());

  public ExpressionAutomaton() {}

  public ExpressionAutomaton(State rootState) {
    this.rootState = rootState;
  }

  public static class Builder {

    private Object token = new Object();

    private State rootState;

    private Builder(ExpressionAutomaton a) {
      rootState = a.rootState;
    }

    public void add(Expression e) {
      rootState = rootState.addExpr(token, e.unsafeRawArray(), 0);
    }

    public ExpressionAutomaton build() {
      return new ExpressionAutomaton(rootState);
    }
  }

  public Builder builder() {
    return new Builder(this);
  }

}