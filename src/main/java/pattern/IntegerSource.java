package pattern;

public class IntegerSource implements NamedValueSource {
  private final NamedValueSource fallback;

  public IntegerSource(NamedValueSource fallback) {
    this.fallback = fallback;
  }

  public Value getValueForName(String name) {
    throw new UnsupportedOperationException();
    // try {
    //   return Integer.parseInt(name);
    // } catch(NumberFormatException e) {
    //   return fallback.getValueForName(name);
    // }
  }
}