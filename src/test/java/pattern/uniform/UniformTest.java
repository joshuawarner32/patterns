package pattern.uniform;

import static org.junit.Assert.*;
import org.junit.Test;

public class UniformTest {
  private Symbol f = new Symbol("f", 2);
  private Symbol g = new Symbol("g", 1);
  private Symbol a = new Symbol("a", 0);
  private Symbol b = new Symbol("b", 0);
  
  @Test
  public void testSimpleTermToString() {
    Term term = new Term(a);
    assertEquals("a", term.toString());
  }

  @Test
  public void testSimpleExpressionToString() {
    Expression e = new Expression(a);
    assertEquals("[a]", e.toString());
  }
  
  @Test
  public void testSimpleTermEquals() {
    Term term = new Term(a);
    assertEquals(a.self(), term);
    assertFalse(b.self().equals(term));
  }
  
  @Test
  public void testSimpleExpressionEquals() {
    Expression e = new Expression(a);
    assertEquals(e, new Expression(a));
    assertFalse(e.equals(new Expression(b)));
  }

  @Test
  public void testRecursiveTermToString() {
    Term term = f.term(a.self(), b.self());
    assertEquals("f(a b)", term.toString());
  }

  @Test
  public void testLongExpressionToString() {
    Expression e = new Expression(f, a, b);
    assertEquals("[f a b]", e.toString());
  }

  @Test
  public void testSimpleExpressionToTerm() {
    Expression e = new Expression(a);
    assertEquals(a.self(), e.toTerm());
  }

  @Test
  public void testSimpleTermToExpression() {
    Term term = new Term(a);
    assertEquals(new Expression(a), term.toExpression());
  }

  @Test
  public void testRecursiveTermToExpression() {
    Term term = f.term(g.term(a.self()), b.self());
    assertEquals(new Expression(f, g, a, b), term.toExpression());
  }

  @Test
  public void testLongExpressionToTerm() {
    Expression e = new Expression(f, g, a, b);
    assertEquals(f.term(g.term(a.self()), b.self()), e.toTerm());
  }

  @Test
  public void testExpressionMatches() {
    Expression e = new Expression(f, g, a, b);
    assertTrue(new Expression(f, Expression.VAR, b).matches(e));
    assertFalse(new Expression(f, a, b).matches(e));
    assertTrue(new Expression(f, g, Expression.VAR, b).matches(e));
    assertTrue(new Expression(f, g, a, Expression.VAR).matches(e));
    assertTrue(new Expression(f, g, Expression.VAR, Expression.VAR).matches(e));
    assertTrue(new Expression(Expression.VAR).matches(e));
    assertFalse(new Expression(f, Expression.VAR).matches(e));
  }

  @Test
  public void testExpressionPrefixMatches() {
    Expression e = new Expression(f, g, a, b);
    assertTrue(new Expression(f, Expression.VAR, b).prefixMatches(e));
    assertFalse(new Expression(f, a, b).prefixMatches(e));
    assertTrue(new Expression(f, g, Expression.VAR, b).prefixMatches(e));
    assertTrue(new Expression(f, g, a, Expression.VAR).prefixMatches(e));
    assertTrue(new Expression(f, g, Expression.VAR, Expression.VAR).prefixMatches(e));
    assertTrue(new Expression(Expression.VAR).prefixMatches(e));

    assertTrue(new Expression(f, Expression.VAR).prefixMatches(e));
    assertTrue(new Expression(f, g, Expression.VAR).prefixMatches(e));
  }

  @Test
  public void testCommonPattern() {
    assertEquals(new Expression(f, Expression.VAR, b), new Expression(f, a, b).commonPattern(new Expression(f, g, a, b)));
    assertEquals(new Expression(f, Expression.VAR, b), new Expression(f, a, b).commonPattern(new Expression(f, Expression.VAR, b)));
  }

  @Test
  public void testSuperPattern() {
    assertEquals(new Expression(f, g, Expression.VAR, b), new Expression(f, Expression.VAR, b).superPattern(new Expression(f, g)));
    assertEquals(Expression.TOP, new Expression(a).superPattern(new Expression(b)));
    assertEquals(Expression.TOP, Expression.TOP.superPattern(new Expression(b)));
    assertEquals(Expression.TOP, new Expression(a).superPattern(Expression.TOP));
  }
}