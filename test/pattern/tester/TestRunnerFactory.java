package pattern.tester;

public interface TestRunnerFactory<T> {

  public TestRunner makeTestRunner(T param);

}