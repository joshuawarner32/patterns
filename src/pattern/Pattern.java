package pattern;

import java.util.List;
import java.util.ArrayList;

public class Pattern {
  private Variable[] args;
  private Value value;

  public Pattern(Value value) {
    this(new Variable[0], value);
  }

  public Pattern(Variable[] args, Value value) {
    this.args = args;
    this.value = value;
  }

  public Value getNakedValue() {
    return value;
  }

  public Value replace(Binding binding) {
    if(value instanceof Variable) {
      return binding.get((Variable)value);
    } else {
      return value;
    }
  }
}