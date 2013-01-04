package pattern.reducer;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import pattern.reducer.simple.SimpleReducer;
import pattern.reducer.ReducerBuilder;

import pattern.Namespace;
import pattern.Symbol;
import pattern.Node;
import pattern.Pattern;
import pattern.Variable;

@RunWith(Theories.class)
public class BinaryReducerTest {

  private static Namespace ns = new Namespace();

  private static final Symbol and = ns.symbol("and");
  private static final Symbol or = ns.symbol("or");
  private static final Symbol xor = ns.symbol("xor");
  private static final Symbol not = ns.symbol("not");

  private static final Symbol true_ = ns.symbol("true");
  private static final Symbol false_ = ns.symbol("false");

  private static final Variable x = new Variable("x");
  private static final Variable y = new Variable("y");

  public static Reducer[] originalReducers = new Reducer[] {
    new SimpleReducer()
  };

  @DataPoints
  public static Reducer[] reducers = new Reducer[originalReducers.length];

  static {
    for(int i = 0; i < originalReducers.length; i++) {
      reducers[i] = buildReducerRules(originalReducers[i]);
    }
  }

  private static Reducer buildReducerRules(Reducer reducer) {
    ReducerBuilder b = reducer.builder();


    b.add(new Rule(new Node(and, true_, true_), true_));
    b.add(new Rule(new Node(and, x, y), false_));

    b.add(new Rule(new Node(or, false_, false_), false_));
    b.add(new Rule(new Node(or, x, y), true_));

    b.add(new Rule(new Node(xor, x, x), false_));
    b.add(new Rule(new Node(xor, x, y), true_));

    b.add(new Rule(new Node(not, true_), false_));
    b.add(new Rule(new Node(not, false_), true_));

    return b.build();
  }

  @Theory
  public void testAnd(Reducer reducer) {
    assertEquals(true_, reducer.reduce(new Node(and, true_, true_)));
    assertEquals(false_, reducer.reduce(new Node(and, true_, false_)));
    assertEquals(false_, reducer.reduce(new Node(and, false_, true_)));
    assertEquals(false_, reducer.reduce(new Node(and, false_, false_)));
  }

  @Theory
  public void testOr(Reducer reducer) {
    assertEquals(true_, reducer.reduce(new Node(or, true_, true_)));
    assertEquals(true_, reducer.reduce(new Node(or, true_, false_)));
    assertEquals(true_, reducer.reduce(new Node(or, false_, true_)));
    assertEquals(false_, reducer.reduce(new Node(or, false_, false_)));
  }

  @Theory
  public void testXor(Reducer reducer) {
    assertEquals(false_, reducer.reduce(new Node(xor, true_, true_)));
    assertEquals(true_, reducer.reduce(new Node(xor, true_, false_)));
    assertEquals(true_, reducer.reduce(new Node(xor, false_, true_)));
    assertEquals(false_, reducer.reduce(new Node(xor, false_, false_)));
  }

  @Theory
  public void testNot(Reducer reducer) {
    assertEquals(false_, reducer.reduce(new Node(not, true_)));
    assertEquals(true_, reducer.reduce(new Node(not, false_)));
  }

  @Theory
  public void testAndNot(Reducer reducer) {
    assertEquals(false_, reducer.reduce(new Node(and, true_, new Node(not, true_))));
  }

}