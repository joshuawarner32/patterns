package pattern.reducer;

import java.util.Map;

import pattern.Value;
import pattern.Binding;
import pattern.MutableBinding;
import pattern.Pattern;
import pattern.Variable;

public class Rule {
  public final Value match;
  public final Value replace;

  public Rule(Value match, Value replace) {
    if(match == null) {
      throw new NullPointerException();
    }
    if(replace == null) {
      throw new NullPointerException();
    }
    this.match = match;
    this.replace = replace;
  }

  public boolean match(Value value, MutableBinding binding) {
    return Pattern.match(match, value, binding);
  }

  public Value replace(Binding binding) {
    return Pattern.replace(replace, binding);
  }
}