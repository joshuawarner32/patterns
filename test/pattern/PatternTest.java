package pattern;

import static pattern.tester.Expect.*;

public class PatternTest {
    
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
  Symbol c = ns.symbol("c");
  Symbol d = ns.symbol("d");

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

  Variable _ = new Variable("_");
  Recursive rec = new Recursive(_, new Node(a, _), b, 2);

  public void testRecursiveToString() {
    expectEqual(rec.toString(), "[rec _ : (a _),b * 2]");
  }

  public void testRecursiveFormat() {
    expectEqual(rec.format(), new Node(a, rec.selfChild()));
  }  

}