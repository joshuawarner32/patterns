package pattern.reducer;

public interface ReducerBuilder {

  public Reducer build();

  public void add(Rule rule);

}