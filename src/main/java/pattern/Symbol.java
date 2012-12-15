package pattern;

public class Symbol extends Value {

  private final Namespace namespace;
  private final String name;

  Symbol(Namespace namespace, String name) {
    this.namespace = namespace;
    this.name = name;
  }

  public boolean isAtomic() {
    return true;
  }

  public Value format() {
    return this;
  }

  public Value formatChildren() {
    return this;
  }

  public int size() {
    throw new UnsupportedOperationException();
  }

  public Value get(int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String toString() {
    return name;
  }
}