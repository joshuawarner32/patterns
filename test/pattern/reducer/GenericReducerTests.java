package pattern.reducer;

import static pattern.tester.Expect.*;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import pattern.Namespace;
import pattern.Symbol;
import pattern.Node;
import pattern.Pattern;

public class GenericReducerTests {

  private ReducerBuilder builder;
    
  private Namespace ns = new Namespace();
  private Symbol a = ns.symbol("a");
  private Symbol b = ns.symbol("b");
  private Symbol c = ns.symbol("c");

  public GenericReducerTests(ReducerBuilder builder) {
    this.builder = builder;
  }

  public void testTrivial() {
    Reducer reducer = builder.build();
    expectEqual(a, reducer.reduce(a));
  }

  public void testSimple() {
    builder.add(new Rule(new Pattern(a), new Pattern(b)));
    Reducer reducer = builder.build();
    expectEqual(b, reducer.reduce(a));
  }

  public void testDoubleReduce() {
    builder.add(new Rule(new Pattern(a), new Pattern(b)));
    builder.add(new Rule(new Pattern(b), new Pattern(c)));
    Reducer reducer = builder.build();
    expectEqual(c, reducer.reduce(a));
  }

  public void testNodeReduce() {
    builder.add(new Rule(new Pattern(new Node(a)), new Pattern(b)));
    Reducer reducer = builder.build();
    expectEqual(b, reducer.reduce(new Node(a)));
  }

  public void testInnerReduce() {
    builder.add(new Rule(new Pattern(a), new Pattern(b)));
    builder.add(new Rule(new Pattern(new Node(b)), new Pattern(c)));
    Reducer reducer = builder.build();
    expectEqual(reducer.reduce(new Node(a)), c);
  }

  public void run() {
    for(Method m : this.getClass().getMethods()) {
      if(m.getName().startsWith("test")) {
        try {
          try {
            ReducerBuilder b = builder;
            builder = b.build().builder();
            m.invoke(this);
            builder = b;
          } catch(IllegalAccessException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
          }
        } catch(Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

}