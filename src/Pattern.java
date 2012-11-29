
public class Pattern {
  private Value value;

  public Pattern(Value value) {
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