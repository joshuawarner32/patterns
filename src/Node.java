import java.util.Arrays;
 
public class Node extends Value {
  private final Value[] items;

  public Node(Value... items) {
    this.items = items;
  }

  public Value reduce() {
    return this;
  }

  public int size() {
    return items.length;
  }

  public Value get(int index) {
    return items[index];
  }

  public String toString() {
    StringBuilder b = new StringBuilder();
    b.append("(");

    if(items.length > 0) {
      b.append(items[0]);
      for(int i = 1; i < items.length; i++) {
        b.append(" ");
        b.append(items[i]);
      }
    }

    b.append(")");
    return b.toString();
  }

  @Override
  public boolean equals(Object o) {
    if(o instanceof Node) {
      Node n = (Node)o;
      return Arrays.equals(items, n.items);
    }
    throw new UnsupportedOperationException();
  }

}