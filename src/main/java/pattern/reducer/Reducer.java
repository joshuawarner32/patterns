package pattern.reducer;

import pattern.Value;

public interface Reducer {

  public ReducerBuilder builder();

  public Value reduce(Value value);
}