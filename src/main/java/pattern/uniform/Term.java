package pattern.uniform;

import java.util.Arrays;

public class Term {
  private Symbol head;
  private Term[] items;

  public Term(Symbol head, Term... items) {
    if(head.arity != items.length) {
      throw new IllegalArgumentException();
    }
    this.head = head;
    this.items = Arrays.copyOf(items, items.length);
  }

  private Term() {}

  static Term makeWithoutReallocate(Symbol head, Term... items) {
    if(head.arity != items.length) {
      throw new IllegalArgumentException();
    }
    Term term = new Term();
    term.head = head;
    term.items = items;
    return term;
  }

  private int count() {
    int c = 1;
    for(int i = 0; i < items.length; i++) {
      c += items[i].count();
    }
    return c;
  }

  private int fill(Symbol[] syms, int index) {
    syms[index++] = head;
    for(int i = 0; i < items.length; i++) {
      index = items[i].fill(syms, index);
    }
    return index;
  }

  public Expression toExpression() {
    int length = count();
    Symbol[] syms = new Symbol[length];
    fill(syms, 0);
    return Expression.makeWithoutReallocate(syms);
  }

  public String toString() {
    if(items.length > 0) {
      StringBuilder b = new StringBuilder();
      b.append(head.name).append('(');
      b.append(items[0]);
      for(int i = 1; i < items.length; i++) {
        b.append(' ');
        b.append(items[i]);
      }
      b.append(')');
      return b.toString();
    } else {
      return head.name;
    }
  }

  public boolean equals(Object o) {
    if(o instanceof Term) {
      Term t = (Term)o;
      if(t.head != head) {
        return false;
      }
      for(int i = 0; i < items.length; i++) {
        if(!items[i].equals(t.items[i])) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  public int hashCode() {
    return head.hashCode() * 3 + Arrays.hashCode(items);
  }
}