package pattern.reducer.graph;

import java.util.Set;
import java.util.HashSet;

import pattern.Variable;
import pattern.Pattern;
import pattern.Value;

class State {
  
  private Value pattern;

  public State(Value pattern) {
    this.pattern = pattern;
  }

  private void validate(Context ctx) {
    if(!Pattern.alreadyMatches(pattern, ctx.getRawValue(), ctx)) {
      throw new IllegalStateException();
    }
  }

}