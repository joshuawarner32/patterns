package pattern.reducer;

import java.util.Map;

import pattern.Pattern;
import pattern.Value;
import pattern.Variable;

public class Rule {
  public final Pattern match;
  public final Pattern replace;

  public Rule(Pattern match, Pattern replace) {
    this.match = match;
    this.replace = replace;
  }
}