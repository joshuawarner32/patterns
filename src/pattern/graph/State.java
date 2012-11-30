package pattern.graph;

import java.util.Set;
import java.util.HashSet;

import pattern.Variable;

class State {
  
  private boolean live = false;
  private State reduction = this;

  private TransitionSet transitions = new TransitionSet();
  private TransitionSet incomingTransitions = new TransitionSet();

  private Set<State> incomingReductions = new HashSet<State>();

  State blazeState(Transition trans) {
    System.out.println("blazeState " + this + " " + trans);
    State s = transitions.blazeState(trans);
    s.incomingTransitions.putTransition(trans, this);
    return s;
  }

  boolean isLive() {
    return live;
  }

  State reduction() {
    return reduction;
  }

  boolean hasTransition(Transition t, State s) {
    return transitions.hasTransition(t, s);
  }

  boolean hasTransitionFrom(Transition t, State s) {
    return incomingTransitions.hasTransition(t, s);
  }

  State getTransition(Transition t) {
    System.out.println("getTransition " + this + " " + t);
    return transitions.getTransition(t);
  }

  TransitionsForVariable transitionsForVariable(Variable v) {
    return transitions.lookupVariable(v);
  }

  void putTransition(Transition t, State s) {
    transitions.putTransition(t, s);
    s.incomingTransitions.putTransition(t, this);
  }

  State getTransitionFrom(Transition t) {
    System.out.println("getTransitionFrom " + this + " " + t);
    return incomingTransitions.getTransition(t);
  }

  void recursiveSetLive(boolean l) {
    if(live != l) {
      if(l) {
        live = true;
        incomingTransitions.recursiveSetLive(true);
      } else {
        // TODO: make sure live is the AND of all outgoing transitions
        throw new UnsupportedOperationException();
      }
    }
  }

  void reduceTo(State state) {
    reduction = state;
    recursiveSetLive(true);
    state.incomingReductions.add(this);
  }

}