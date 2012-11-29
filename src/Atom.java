
public class Atom extends Value {
  private String name;

  public Atom(String name) {
    this.name = name;
  }

  public Value reduce() {
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