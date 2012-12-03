package pattern.reducer;

import pattern.reducer.simple.SimpleReducer;

public class SimpleReducerTest {


  public void testGeneric() {
    new pattern.reducer.GenericReducerTests(SimpleReducer.newBuilder()).run();

  }
}