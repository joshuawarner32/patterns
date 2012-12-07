package pattern.tester;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassTestRunner implements TestRunner {

  private final Class cls;
  private final Object[] ctorArgs;

  public ClassTestRunner(Class cls, Object... ctorArgs) {
    this.cls = cls;
    this.ctorArgs = ctorArgs;
  }

  private static Class[] types(Object[] args) {
    Class[] ret = new Class[args.length];
    for(int i = 0; i < args.length; i++) {
      ret[i] = args[i].getClass();
    }
    return ret;
  }

  private String prettyTrace(Throwable t, Method m) {
    StringBuilder b = new StringBuilder();

    StackTraceElement[] trace = t.getStackTrace();
    b.append(t).append('\n');
    for(StackTraceElement frame : trace) {
      b.append("  at ").append(frame).append('\n');
      if(frame.getClassName().equals(cls.getCanonicalName()) && frame.getMethodName().equals(m.getName())) {
        break;
      }
    }
    return b.toString();
  }

  public void runTests(TestContext context) {
    for(Method m : cls.getMethods()) {
      if(m.getName().startsWith("test")) {
        boolean success = false;
        try {
          try {
            Constructor ctor = cls.getConstructor(types(ctorArgs));
            Object t = ctor.newInstance(ctorArgs);
            try {
              m.invoke(t);
              success = true;
            } catch(IllegalAccessException e) {
              e.printStackTrace();
            } catch (InvocationTargetException e) {
              Throwable cause = e.getCause();
              System.out.println(prettyTrace(cause, m));
            }
          } catch(InvocationTargetException e) {
            e.printStackTrace();
          } catch(NoSuchMethodException e) {
            e.printStackTrace();
          }
        } catch(InstantiationException e) {
          e.printStackTrace();
        } catch(IllegalAccessException e) {
          e.printStackTrace();
        }

        context.addTestResult(m.getName(), success);
      }
    }
  }

  public static final TestRunnerFactory<Class> FACTORY = new TestRunnerFactory<Class>() {
    public TestRunner makeTestRunner(Class cls) {
      return new ClassTestRunner(cls);
    }
  };
}