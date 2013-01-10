package pattern.uniform;

import java.util.Arrays;

public class Expression {
  private Symbol[] symbols;

  public Expression(Symbol... symbols) {
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

  Symbol[] unsafeRawArray() {
    return symbols;
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
    protected Symbol[] left;
    protected Symbol[] right;
    protected int ir = 0;
    protected int il = 0;

    protected Matcher(Symbol[] left, Symbol[] right) {
      this.left = left;
      this.right = right;
    }

    protected void skipLeft() {
      Symbol head = left[il++];
      for(int i = 0; i < head.arity; i++) {
        skipLeft();
      }
    }

    protected void skipRight() {
      Symbol head = right[ir++];
      for(int i = 0; i < head.arity; i++) {
        skipRight();
      }
    }

    protected boolean match(boolean allowPrefixMatch) {
      while(true) {
        if(ir >= right.length) {
          if(il >= left.length) {
            return true;
          } else {
            return allowPrefixMatch;
          }
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
    return new Matcher(e.symbols, symbols).match(false);
  }

  public boolean prefixMatches(Expression e) {
    return new Matcher(e.symbols, symbols).match(true);
  }

  private static class CommonMatcher extends Matcher {
    private Symbol[] res;
    private int iRes = 0;

    private CommonMatcher(Symbol[] left, Symbol[] right) {
      super(left, right);
      res = new Symbol[Math.min(left.length, right.length)];
    }

    private Symbol[] common() {
      while(true) {
        if(il >= left.length || ir >= right.length) {
          break;
        } else if(left[il] == right[ir]) {
          res[iRes++] = left[il];
          il++;
          ir++;
        } else {
          skipLeft();
          skipRight();
          res[iRes++] = VAR;
        }
      }
      return Arrays.copyOf(res, iRes);
    }

  }

  public Expression commonPattern(Expression e) {
    return Expression.makeWithoutReallocate(new CommonMatcher(symbols, e.symbols).common());
  }

  private static class SuperMatcher extends Matcher {
    private Symbol[] res;
    private int iRes = 0;

    private SuperMatcher(Symbol[] left, Symbol[] right) {
      super(left, right);
      res = new Symbol[left.length + right.length];
    }

    protected void readLeft() {
      if(il < left.length) {
        Symbol head = left[il++];
        res[iRes++] = head;
        for(int i = 0; i < head.arity; i++) {
          readLeft();
        }
      } else {
        res[iRes++] = VAR;
      }
    }

    protected void readRight() {
      if(ir < right.length) {
        Symbol head = right[ir++];
        res[iRes++] = head;
        for(int i = 0; i < head.arity; i++) {
          readRight();
        }
      } else {
        res[iRes++] = VAR;
      }
    }

    private Symbol[] super_() {
      while(true) {
        if(il >= left.length) {
          if(ir >= right.length) {
            return Arrays.copyOf(res, iRes);
          } else {
            res[iRes++] = right[ir++];
          }
        } else if(ir >= right.length) {
          res[iRes++] = left[il++];
        } else if(left[il] == right[ir]) {
          res[iRes++] = left[il];
          il++;
          ir++;
        } else if(left[il] == VAR) {
          readRight();
          il++;
        } else if(right[il] == VAR) {
          readLeft();
          ir++;
        } else {
          return null;
        }
      }
    }

  }

  public static final Expression TOP = makeWithoutReallocate(null);

  public Expression superPattern(Expression e) {
    if(this == TOP || e == TOP) {
      return TOP;
    }
    Symbol[] res = new SuperMatcher(symbols, e.symbols).super_();
    if(res == null) {
      return TOP;
    } else {
      return makeWithoutReallocate(res);
    }
  }
}