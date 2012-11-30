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

  public void testTrivialGraph() {
    Graph g = new Graph();
    expectSame(g.reduce(a), a);
    expectEqual(g.reduce(new Node(a, b)), new Node(a, b));
  }

  public void testSimpleGraph() {
    Graph g = new Graph();
    g.putReduction(new Pattern(a), new Pattern(b));
    g.putReduction(new Pattern(c), new Pattern(d));
    expectSame(g.reduce(b), b);
    expectSame(g.reduce(a), b);
    expectSame(g.reduce(c), d);
  }

  public void testIterativeGraph() {
    Graph g = new Graph();
    g.putReduction(new Pattern(a), new Pattern(b));
    g.putReduction(new Pattern(b), new Pattern(c));
    expectSame(g.reduce(a), c);
  }

  public void testNodeGraph() {
    Graph g = new Graph();
    System.out.println("==============");
    g.putReduction(new Pattern(new Node(a)), new Pattern(b));
    expectSame(g.reduce(a), a);
    // expectEqual(g.reduce(new Node(a)), b);
    System.out.println("==============");
    // expectEqual(g.reduce(new Node(b)), new Node(b));
  }
  

}