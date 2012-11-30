import java.util.List;
import java.util.ArrayList;

public class Pattern {
  private Atom[] args;
  private Value value;

  public Pattern(Value value) {
    this(new Atom[0], value);
  }

  public Pattern(Atom[] args, Value value) {
    this.args = args;
    this.value = value;
  }

  public Value getNakedValue() {
    return value;
  }

  public Value replace(Binding binding) {
    if(value instanceof Atom) {
      return binding.get((Atom)value);
    } else {
      return value;
    }
  }
}