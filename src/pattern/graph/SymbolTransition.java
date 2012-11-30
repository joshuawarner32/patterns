package pattern.graph;

import pattern.Value;
import pattern.Symbol;
import pattern.Variable;

class SymbolTransition extends Transition {
  Symbol symbol;

  SymbolTransition(Variable var, Symbol symbol) {
    super(var);
    this.symbol = symbol;
  }

  void forward(Context ctx) {
    Value v = ctx.get(var);
    if(v != symbol) {
      throw new IllegalStateException();
    }
    ctx.unbind(var);
  }

  void reverse(Context ctx) {
    ctx.bind(var, symbol);
  }

  public boolean equals(Object o) {
    if(!(o instanceof SymbolTransition)) {
      return false;
    }
    SymbolTransition t = (SymbolTransition)o;
    return var == t.var && symbol == t.symbol;
  }

  public int hashCode() {
    return var.hashCode() + 3 * symbol.hashCode();
  }

}