package pattern.uniform;

import java.util.Arrays;

public class Expression {
  private Symbol[] symbols;

  public Expression(Symbol... symbols) {
    // TODO: validate
    this.symbols = Arrays.copyOf(symbols, symbols.length);
  }

  private class Pointer {
    private int start = 0;
    private Term readTerm() {
      Symbol head = symbols[start++];

      if(head.arity == 0) {
        return head.self();
      } else {
        TermBuilder b = new TermBuilder(head);
        for(int i = 0; i < head.arity; i++) {
          b.append(readTerm());
        }
        return b.toTerm();
      }
    }
  }

  public Term toTerm() {
    return new Pointer().readTerm();
  }

  private Expression() {}

  static Expression makeWithoutReallocate(Symbol... symbols) {
    Expression e = new Expression();
    e.symbols = symbols;
    return e;
  }

  public String toString() {
    StringBuilder b = new StringBuilder();

    b.append('[');
    if(symbols.length > 0) {
      b.append(symbols[0].name);
    }
    for(int i = 1; i < symbols.length; i++) {
      b.append(' ');
      b.append(symbols[i].name);
    }
    b.append(']');
    return b.toString();
  }

  public boolean equals(Object o) {
    return (o instanceof Expression) && Arrays.equals(((Expression)o).symbols, symbols);
  }
}