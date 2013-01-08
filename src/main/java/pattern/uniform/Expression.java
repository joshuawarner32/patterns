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

  public int hashCode() {
    return Arrays.hashCode(symbols);
  }

  public static final Symbol VAR = new Symbol("_", 0);

  private static class Matcher {
    private Symbol[] left;
    private Symbol[] right;
    private int ir = 0;
    private int il = 0;

    private Matcher(Symbol[] left, Symbol[] right) {
      this.left = left;
      this.right = right;
    }

    private void skipLeft() {
      Symbol head = left[il++];
      for(int i = 0; i < head.arity; i++) {
        skipLeft();
      }
    }

    private boolean match() {
      while(true) {
        if(il >= left.length || ir >= right.length) {
          return true;
        } else if(left[il] == right[ir]) {
          il++;
          ir++;
        } else if(right[ir] == VAR) {
          skipLeft();
          ir++;
        } else {
          return false;
        }
      }
    }
  }

  public boolean matches(Expression e) {
    return new Matcher(symbols, e.symbols).match();
  }
}