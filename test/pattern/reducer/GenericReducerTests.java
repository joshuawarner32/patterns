package pattern.reducer;

import static pattern.tester.Expect.*;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import pattern.Namespace;
import pattern.Symbol;
import pattern.Node;

public class GenericReducerTests {

  private ReducerBuilder builder;
    
  private Namespace ns = new Namespace();
  private Symbol a = ns.symbol("a");

  public GenericReducerTests(ReducerBuilder builder) {
    this.builder = builder;
  }

  public void testTrivial() {
    Reducer reducer = builder.build();
    expectEqual(a, reducer.reduce(a));
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