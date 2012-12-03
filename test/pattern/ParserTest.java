package pattern;

import static pattern.tester.Expect.*;

public class ParserTest {

  private Namespace ns = new Namespace();

  private Symbol a = ns.symbol("a");

  public void testEmptyNode() {
    expectEqual(Parser.parse(ns, "()"), new Node());
  }

  public void testSymbol() {
    expectEqual(Parser.parse(ns, "a"), a);
  }

}