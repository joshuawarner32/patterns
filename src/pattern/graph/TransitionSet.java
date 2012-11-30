package pattern.graph;

import java.util.Map;
import java.util.HashMap;

class TransitionSet {
  private Map<String, TransitionToState> transitions = new HashMap<String, TransitionToState>();


  void recursiveSetLive(boolean l) {
    for(TransitionToState t : transitions.values()) {
      t.state.recursiveSetLive(l);
    }
  }
}