package pattern;

import static org.junit.Assert.*;
import org.junit.Test;

public class PatternTest {
    
  private Namespace ns = new Namespace();

  @Test
  public void testNamespaceIdenticalSymbols() {
    assertSame(ns.symbol("a"), ns.symbol("a"));
  }

  @Test
  public void testNamespaceDifferentSymbols() {
    assertNotSame(ns.symbol("a"), ns.symbol("b"));
    assertFalse(ns.symbol("a").equals(ns.symbol("b")));
  }

  Symbol a = ns.symbol("a");
  Symbol b = ns.symbol("b");
  Symbol c = ns.symbol("c");
  Symbol d = ns.symbol("d");

  @Test
  public void testSymbolToString() {
    assertEquals(a.toString(), "a");
    assertEquals(b.toString(), "b");
    assertSame(a.toString(), a.toString());
  }

  @Test
  public void testNodeToString() {
    assertEquals(new Node().toString(), "()");
    assertEquals(new Node(a).toString(), "(a)");
    assertEquals(new Node(a, b).toString(), "(a b)");
    assertEquals(new Node(new Node()).toString(), "(())");
    assertEquals(new Node(new Node(a)).toString(), "((a))");
    assertEquals(new Node(a, new Node()).toString(), "(a ())");
  }

  Variable _ = new Variable("_");
  Recursive rec = new Recursive(_, new Node(a, _), b, 2);

  @Test
  public void testRecursiveToString() {
    assertEquals(rec.toString(), "[rec _ : (a _),b * 2]");
  }

  @Test
  public void testRecursiveFormat() {
    assertEquals(rec.format(), new Node(a, rec.selfChild()));
  }  

}