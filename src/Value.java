import java.util.Iterator;

public abstract class Value implements Iterable<Value> {

  public abstract int size();
  public abstract Value get(int index);

  public Value format() {
    Value[] children = new Value[size()];
    for(int i = 0; i < children.length; i++) {
      children[i] = get(i);
    }
    return new Node(children);
  }

  public Value formatChildren() {
    Value start = format();

    Value[] children = new Value[start.size()];
    for(int i = 0; i < start.size(); i++) {
      children[i] = start.get(i).fullFormat();
    }
    return new Node(children);
  }

  public final Value fullFormat() {
    return format().formatChildren();
  }

  @Override
  public final Iterator<Value> iterator() {
    return new Iterator<Value>() {
      private int index = 0;
      private int size = size();

      public boolean hasNext() {
        return index < size;
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }

      public Value next() {
        return get(index++);
      }
    };
  }
}