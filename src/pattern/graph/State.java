package pattern.graph;

import java.util.Set;
import java.util.HashSet;

class State {
  
  private boolean live = false;
  private State reduction = this;

  private TransitionSet transitions = new TransitionSet();
  private TransitionSet incomingTransitions = new TransitionSet();

  private Set<State> incomingReductions = new HashSet<State>();

  State blazeState(Transition trans, boolean live) {
    return null;
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
  }

}