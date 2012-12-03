package pattern.tester;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import pattern.*;

public class Tester {

  public static void main(String[] args) {
    int tests = 0;
    int successes = 0;

    Class[] classes = new Class[] {
      pattern.graph.GraphTest.class,
      pattern.PatternTest.class,
      pattern.reducer.SimpleReducerTest.class,
      pattern.ParserTest.class
    };

    for(Class cls : classes) {
      for(Method m : cls.getMethods()) {
        if(m.getName().startsWith("test")) {
          try {
            Object t = cls.newInstance();
            try {
              tests++;
              m.invoke(t);
              successes++;
            } catch(IllegalAccessException e) {
              e.printStackTrace();
            } catch (InvocationTargetException e) {
              e.getCause().printStackTrace();
            }
          } catch(InstantiationException e) {
            e.printStackTrace();
          } catch(IllegalAccessException e) {
            e.printStackTrace();
          }
        }
      }
    }
    System.out.println("Tests: " + tests + ", Successes: " + successes + ", Failures: " + (tests - successes));
  }
}