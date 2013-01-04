package pattern.reducer.graph;

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

  Value toValue() {
    if(context == null) {
      throw new IllegalStateException();
    }
    return context.result();
  }

  Value toRawValue() {
    if(context == null) {
      return originalValue;
    } else {
      throw new UnsupportedOperationException();
    }
  }
}