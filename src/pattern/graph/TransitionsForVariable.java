package pattern.graph;

import java.util.Map;
import java.util.HashMap;

import pattern.Symbol;

class TransitionsForVariable {
  private Map<Symbol, State> symbolTransitions = new HashMap<Symbol, State>();
  private Map<Integer, TransitionToState> listTransitions = new HashMap<Integer, TransitionToState>();

  void putTransition(Transition trans, State state) {
    if(trans instanceof SymbolTransition) {
      symbolTransitions.put(((SymbolTransition)trans).symbol, state);
    } else if(trans instanceof ListTransition) {
      listTransitions.put(((ListTransition)trans).arity(), new TransitionToState(trans, state));
    } else {
      throw new UnsupportedOperationException();
    }
  }

  void recursiveSetLive(boolean l) {
    for(State s : symbolTransitions.values()) {
      s.recursiveSetLive(l);
    }
    for(TransitionToState lt : listTransitions.values()) {
      lt.state.recursiveSetLive(l);
    }
  }

  State getTransition(Transition trans) {
    System.out.println("    getTransition " + trans);
    if(trans instanceof SymbolTransition) {
      return symbolTransitions.get(((SymbolTransition)trans).symbol);
    } else if(trans instanceof ListTransition) {
      TransitionToState ts = listTransitions.get(((ListTransition)trans).arity());
      return ts == null ? null : ts.state;
    } else {
      throw new UnsupportedOperationException();
    }
  }

  boolean hasTransition(Transition trans, State s) {
    return getTransition(trans) == s;
  }

  State blazeState(Transition trans) {
    State s = getTransition(trans);
    if(s == null) {
      s = new State();
      putTransition(trans, s);
    }
    return s;
  }
}