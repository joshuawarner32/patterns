package pattern;

import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

  private Namespace ns = new Namespace();

  private Symbol a = ns.symbol("a");

  @Test
  public void testEmptyNode() {
    assertEquals(new Node(), Parser.parse(ns, "()"));
  }

  @Test
  public void testSymbol() {
    assertEquals(a, Parser.parse(ns, "a"));
  }

}