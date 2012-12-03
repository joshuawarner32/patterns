package pattern;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class Parser {

  private static boolean isIdent(char ch) {
    return !Character.isWhitespace(ch) && ch != '(' && ch != ')';
  }

  private static class Context {
    public Stack<List<Value>> stack = new Stack<List<Value>>();

    void push() {
      stack.push(new ArrayList<Value>());
    }

    void add(Value value) {
      stack.peek().add(value);
    }

    Value pop() {
      List<Value> vals = stack.pop();
      return new Node(vals.toArray(new Value[vals.size()]));
    }
  }

  public static Value parse(Namespace ns, String str) {
    Context ctx = new Context();
    ctx.push();
    int i = 0;
    while(i < str.length()) {
      char ch = str.charAt(i);

      if(Character.isWhitespace(ch)) {
        while(Character.isWhitespace(ch)) {
          i++;
          if(i >= str.length()) {
            break;
          }
          ch = str.charAt(i);
        }
      } else if(ch == '(') {
        ctx.push();
        i++;
      } else if(ch == ')') {
        ctx.add(ctx.pop());
        i++;
      } else {
        int start = i;
        while(isIdent(ch)) {
          i++;
          if(i >= str.length()) {
            break;
          }
          ch = str.charAt(i);
        }
        ctx.add(ns.symbol(str.substring(start, i)));
      }
    }
    return ctx.pop().get(0);
  }

}