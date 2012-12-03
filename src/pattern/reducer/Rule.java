package pattern.reducer;

import java.util.Map;

import pattern.Pattern;
import pattern.Value;
import pattern.Variable;

public class Rule {
  private Pattern match;
  private Pattern replace;

  public Rule(Pattern match, Pattern replace) {
    this.match = match;
    this.replace = replace;
  }

  public boolean match(Value value, Map<Variable, Value> context) {
    return false;
  }

  public Value replace(Map<Variable, Value> context) {
    throw new RuntimeException();
  }
}