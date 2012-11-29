import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class Test {

  private static class ExpectedException extends RuntimeException {
    public ExpectedException(String message) {
      super(message);
    }
  }

  public <T> void expectSame(T a, T b) {
    if(a != b) {
      throw new ExpectedException("expected same:\n  " + a.toString() + "\n  " + b.toString());
    }
  }

  public <T> void expectDifferent(T a, T b) {
    if(a == b) {
      throw new ExpectedException("expected different:\n  " + a.toString() + "\n  " + b.toString());
    }
  }

  public <T> void expectEqual(T a, T b) {
    if(!a.equals(b)) {
      throw new ExpectedException("expected equal:\n  " + a.toString() + "\n  " + b.toString());
    } 
  }

  public <T> void expectNotEqual(T a, T b) {
    if(a.equals(b)) {
      throw new ExpectedException("expected not equal:\n  " + a.toString() + "\n  " + b.toString());
    } 
  }
    
  private Namespace ns = new Namespace();

  public void testNamespaceIdenticalSymbols() {
    expectSame(ns.symbol("a"), ns.symbol("a"));
  }

  public void testNamespaceDifferentSymbols() {
    expectDifferent(ns.symbol("a"), ns.symbol("b"));
    expectNotEqual(ns.symbol("a"), ns.symbol("b"));
  }

  Symbol a = ns.symbol("a");
  Symbol b = ns.symbol("b");

  public void testSymbolToString() {
    expectEqual(a.toString(), "a");
    expectEqual(b.toString(), "b");
    expectSame(a.toString(), a.toString());
  }

  public void testNodeToString() {
    expectEqual(new Node().toString(), "()");
    expectEqual(new Node(a).toString(), "(a)");
    expectEqual(new Node(a, b).toString(), "(a b)");
    expectEqual(new Node(new Node()).toString(), "(())");
    expectEqual(new Node(new Node(a)).toString(), "((a))");
    expectEqual(new Node(a, new Node()).toString(), "(a ())");
  }

  Atom _ = new Atom("_");
  Recursive rec = new Recursive(_, new Node(a, _), b, 2);

  public void testRecursiveToString() {
    expectEqual(rec.toString(), "[rec _ : (a _),b * 2]");
  }

  public void testRecursiveFormat() {
    expectEqual(rec.format(), new Node(a, rec.selfChild()));
  }

  public static void main(String[] args) {
    for(Method m : Test.class.getMethods()) {
      if(m.getName().startsWith("test")) {
        Test t = new Test();
        try {
          m.invoke(t);
        } catch(IllegalAccessException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.getCause().printStackTrace();
        }
      }
    }
  }
}