package pattern.graph;

import java.util.Map;
import java.util.HashMap;

import pattern.Variable;

class TransitionSet {
  private Map<Variable, TransitionsForVariable> transitions = new HashMap<Variable, TransitionsForVariable>();

  void recursiveSetLive(boolean l) {
    for(TransitionsForVariable t : transitions.values()) {
      t.recursiveSetLive(l);
    }
  }

  TransitionsForVariable lookupVariable(Variable v) {
    return transitions.get(v);
  }

  TransitionsForVariable blazeVariable(Variable v) {
    TransitionsForVariable r = lookupVariable(v);
    if(r == null) {
      transitions.put(v, r = new TransitionsForVariable());
    }
    return r;
  }

  boolean hasTransition(Transition trans, State s) {
    return getTransition(trans) == s;
  }

  State getTransition(Transition trans) {
    System.out.println("  getTransition " + trans);
    TransitionsForVariable r = lookupVariable(trans.var);
    if(r == null) {
      System.out.println("    (null)");
      return null;
    }
    return r.getTransition(trans);
  }

  State blazeState(Transition trans) {
    System.out.println("  blazeState " + trans);
    State s = blazeVariable(trans.var).blazeState(trans);
    System.out.println("    " + s);
    return s;
  }

  void putTransition(Transition trans, State state) {
    System.out.println("  putTransition " + trans + " " + state);
    blazeVariable(trans.var).putTransition(trans, state);
  }
}