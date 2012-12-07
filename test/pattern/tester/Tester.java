package pattern.tester;

import pattern.*;

public class Tester {

  private static class MyTestContext implements TestContext {
    private int successes;
    private int tests;

    public void addTestResult(String name, boolean success) {
      tests++;
      if(success) {
        successes++;
      }
    }

    String formatSummary() {
      return "Tests: " + tests + ", Successes: " + successes + ", Failures: " + (tests - successes);
    }
  }

  public static void main(String[] args) {
    MyTestContext context = new MyTestContext();
    TestRunner tests = Testers.classes(
      pattern.graph.GraphTest.class,
      pattern.PatternTest.class,
      pattern.reducer.SimpleReducerTest.class,
      pattern.ParserTest.class
    );

    tests.runTests(context);
    System.out.println(context.formatSummary());
  }
}