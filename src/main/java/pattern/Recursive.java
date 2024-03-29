package pattern;

public class Recursive extends Value {
  private Variable marker;
  private Node items;
  private Value terminal;
  private int count;

  public Recursive(Variable marker, Node items, Value terminal, int count) {
    this.marker = marker;
    this.items = items;
    this.terminal = terminal;
    this.count = count;
  }

  public boolean isAtomic() {
    return count == 0 && terminal.isAtomic();
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
        return selfChild();
      } else {
        return terminal;
      }
    } else {
      return ret;
    }
  }

  public Recursive selfChild() {
    return new Recursive(marker, items, terminal, count - 1);
  }

  @Override
  public String toString() {
    return "[rec " + marker.toString() + " : " + items.toString() + "," + terminal.toString() + " * " + count + "]";
  }

  @Override
  public boolean equals(Object o) {
    if(o instanceof Recursive) {
      Recursive r = (Recursive)o;
      return
        count == r.count &&
        marker == r.marker &&
        items.equals(r.items) &&
        terminal.equals(r.terminal);
    }
    throw new UnsupportedOperationException();
  }
}