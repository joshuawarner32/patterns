package pattern;

public class Variable extends Value {
  private String name;

  public Variable(String name) {
    this.name = name;
  }

  public boolean isAtomic() {
    return true;
  }

  public Value format() {
    throw new UnsupportedOperationException();
  }

  public int size() {
    throw new UnsupportedOperationException();
  }

  public Value get(int index) {
    throw new UnsupportedOperationException();
  }

  public String toString() {
    return name;
  }
}