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
public class PenoReducerTest {


  private static Namespace ns = new Namespace();

  private static final Symbol succ = ns.symbol("succ");
  private static final Symbol add = ns.symbol("or");

  private static final Symbol zero = ns.symbol("true");

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


    b.add(new Rule(new Pattern(new Node(add, zero, x)), new Pattern(x)));
    b.add(new Rule(new Pattern(new Node(add, new Node(succ, x), y)), new Pattern(new Node(add, x, new Node(succ, y)))));

    return b.build();
  }

  @Theory
  public void testSucc(Reducer reducer) {
    assertEquals(zero, reducer.reduce(zero));
    assertEquals(new Node(succ, zero), reducer.reduce(new Node(succ, zero)));
  }

  @Theory
  public void testAdd(Reducer reducer) {
    assertEquals(zero, reducer.reduce(new Node(add, zero, zero)));
    assertEquals(new Node(succ, zero), reducer.reduce(new Node(add, zero, new Node(succ, zero))));
    assertEquals(new Node(succ, zero), reducer.reduce(new Node(add, new Node(succ, zero), zero)));
    assertEquals(new Node(succ, new Node(succ, zero)), reducer.reduce(new Node(add, new Node(succ, zero), new Node(succ, zero))));
  }
}