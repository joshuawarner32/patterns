package pattern.graph;

import java.util.Map;
import java.util.HashMap;

import pattern.Variable;
import pattern.Value;

class Context {

  private Map<Variable, Value> bindings = new HashMap<Variable, Value>();
  
  Value get(Variable var) {
    Value ret = bindings.get(var);
    if(ret == null) {
      throw new IllegalStateException();
    }
    return ret;
  }

  void bind(Variable var, Value value) {
    if(bindings.put(var, value) != null) {
      throw new IllegalStateException();
    }
  }

  void unbind(Variable var) {
    if(bindings.remove(var) == null) {
      throw new IllegalStateException();
    }
  }
}