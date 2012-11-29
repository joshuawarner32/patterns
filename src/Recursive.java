public class Recursive extends Value {
  private Atom marker;
  private Node items;
  private Value terminal;
  private int count;

  public Recursive(Atom marker, Node items, Value terminal, int count) {
    this.marker = marker;
    this.items = items;
    this.terminal = terminal;
    this.count = count;
  }

  public Value format() {
    if(count == 0) {
      return terminal;
    } else {
      return super.format();
    }
  }

  public int size() {
    return items.size();
  }

  public Value get(int index) {
    Value ret = items.get(index);
    if(ret == marker) {
      if(count > 1) {
        return new Recursive(marker, items, terminal, count - 1);
      } else {
        return terminal;
      }
    } else {
      return ret;
    }
  }

  public String toString() {
    return "[rec " + marker.toString() + " : " + items.toString() + "," + terminal.toString() + " * " + count + "]";
  }
}