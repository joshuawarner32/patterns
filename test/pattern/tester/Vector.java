package pattern.tester;

import java.util.Arrays;
import java.util.Iterator;

public class Vector<T> implements Iterable<T> {

  private T[] values;

  public Vector(T... values) {
    this.values = Arrays.copyOf(values, values.length);
  }

  public Iterator<T> iterator() {
    return new Iterator<T>() {
      int index = 0;

      public boolean hasNext() {
        return index < values.length;
      }

      public T next() {
        return values[index++];
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
}