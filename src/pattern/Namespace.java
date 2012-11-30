package pattern;

import java.util.Map;
import java.util.HashMap;

public class Namespace {
  private Map<String, Symbol> syms = new HashMap<String, Symbol>();

  public Symbol symbol(String name) {
    Symbol ret = syms.get(name);
    if(ret == null) {
      syms.put(name, ret = new Symbol(this, name));
    }
    return ret;
  }

}