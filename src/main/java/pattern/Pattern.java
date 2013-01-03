package pattern;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Pattern {
  private Variable[] args;
  private Value value;

  public Pattern(Value value) {
    this(new Variable[0], value);
  }

  public Pattern(Variable[] args, Value value) {
    if(value == null) {
      throw new NullPointerException();
    }
    this.args = args;
    this.value = value;
  }

  public Value getNakedValue() {
    return value;
  }
}