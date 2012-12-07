package pattern.tester;

public class Testers {
  private Testers() {}

  public static Vector<Class> vector(Class... classes) {
    return new Vector<Class>(classes);
  }

  public static TestRunner classes(Class... classes) {
    return parameterized(ClassTestRunner.FACTORY, vector(classes));
  }

  public static TestRunnerFactory paramClassFactory(Class cls) {
    return null;
  }

  public static <T> TestRunner parameterized(TestRunnerFactory<T> factory, Vector<T> params) {
    return new ParameterizedTestRunner(factory, params);
  }
}