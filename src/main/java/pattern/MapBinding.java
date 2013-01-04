package pattern;

import java.util.Map;
import java.util.HashMap;

public class MapBinding implements MutableBinding {
  private Map<Variable, Value> map = new HashMap<Variable, Value>();

  public Value get(Variable a) {
    return map.get(a);
  }

  public void bind(Variable a, Value v) {
    map.put(a, v);
  }

  public boolean isEmpty() {
    return map.isEmpty();
  }

  public void clear() {
    map.clear();
  }
}