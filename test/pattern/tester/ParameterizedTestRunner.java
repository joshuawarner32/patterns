package pattern.tester;

import java.util.Arrays;

public class ParameterizedTestRunner implements TestRunner {

  private final TestRunnerFactory factory;
  private final Vector<?> paramValues;

  public ParameterizedTestRunner(TestRunnerFactory<?> factory, Vector<?> paramValues) {
    this.factory = factory;
    this.paramValues = paramValues;
  }

  public void runTests(TestContext context) {
    for(Object v : paramValues) {
      TestRunner runner = factory.makeTestRunner(v);
      runner.runTests(context);
    }
  }
}