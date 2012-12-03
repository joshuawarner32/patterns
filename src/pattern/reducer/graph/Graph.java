package pattern.graph;

import java.util.Map;
import java.util.HashMap;

import pattern.Value;
import pattern.Variable;
import pattern.Symbol;

public class Graph {

  Map<Symbol, State> symbolStates = new HashMap<Symbol, State>();
  Map<Integer, ListTransition> listStates = new HashMap<Integer, ListTransition>();

  public Value reduce(Value value) {
    return null;
  }

}