package pattern.reducer;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import pattern.reducer.simple.SimpleReducer;

import pattern.Namespace;
import pattern.Symbol;
import pattern.Node;
import pattern.Pattern;
import pattern.Variable;

@RunWith(Theories.class)
public class GenericReducerTest {


  @DataPoints
  public static Reducer[] reducers = new Reducer[] {
    new SimpleReducer()
  };
    
  private Namespace ns = new Namespace();
  private Symbol a = ns.symbol("a");
  private Symbol b = ns.symbol("b");
  private Symbol c = ns.symbol("c");

  @Theory
  public void testTrivial(Reducer reducer) {
    ReducerBuilder builder = reducer.builder();
    reducer = builder.build();
    assertEquals(a, reducer.reduce(a));
  }

  @Theory
  public void testSimple(Reducer reducer) {
    ReducerBuilder builder = reducer.builder();
    builder.add(new Rule(new Pattern(a), new Pattern(b)));
    reducer = builder.build();
    assertEquals(b, reducer.reduce(a));
  }

  @Theory
  public void testDoubleReduce(Reducer reducer) {
    ReducerBuilder builder = reducer.builder();
    builder.add(new Rule(new Pattern(a), new Pattern(b)));
    builder.add(new Rule(new Pattern(b), new Pattern(c)));
    reducer = builder.build();
    assertEquals(c, reducer.reduce(a));
  }

  @Theory
  public void testNodeReduce(Reducer reducer) {
    ReducerBuilder builder = reducer.builder();
    builder.add(new Rule(new Pattern(new Node(a)), new Pattern(b)));
    reducer = builder.build();
    assertEquals(b, reducer.reduce(new Node(a)));
  }

  @Theory
  public void testInnerReduce(Reducer reducer) {
    ReducerBuilder builder = reducer.builder();
    builder.add(new Rule(new Pattern(a), new Pattern(b)));
    builder.add(new Rule(new Pattern(new Node(b)), new Pattern(c)));
    reducer = builder.build();
    assertEquals(c, reducer.reduce(new Node(a)));
  }

  @Theory
  public void testExtractInner(Reducer reducer) {
    ReducerBuilder builder = reducer.builder();
    Variable v = new Variable("_");
    builder.add(new Rule(new Pattern(new Node(v)), new Pattern(v)));
    reducer = builder.build();
    assertEquals(a, reducer.reduce(new Node(a)));
    assertEquals(b, reducer.reduce(new Node(b)));
    assertEquals(new Node(a, b), reducer.reduce(new Node(new Node(a, b))));
    assertEquals(a, reducer.reduce(new Node(new Node(a))));
  }

}