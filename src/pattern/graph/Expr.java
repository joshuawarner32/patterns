package pattern.graph;

import pattern.Value;
import pattern.Variable;

class Expr {

  private final Value originalValue;

  private Context context = null;

  public Expr(Value originalValue) {
    this.originalValue = originalValue;
  }

  void prepare(Context parent) {
    if(context == null) {
      context = parent.newChild(originalValue);
    }
  }

  State partialReduce(Context parent, TransitionsForVariable trans) {
    return null;
  }

  Value toValue() {
    if(context == null) {
      throw new IllegalStateException();
    }
    return context.result();
  }

  boolean isAtomic() {
    throw new UnsupportedOperationException();
  }

  int size() {
    throw new UnsupportedOperationException();
  }

  Expr get(int i) {
    throw new UnsupportedOperationException();
  }
}