package pattern.reducer.simple;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import pattern.Value;
import pattern.Variable;
import pattern.Pattern;
import pattern.Node;

import pattern.MapBinding;

import pattern.reducer.Rule;
import pattern.reducer.Reducer;
import pattern.reducer.ReducerBuilder;

public class SimpleReducer implements Reducer {

  private List<Rule> rules;

  public SimpleReducer() {
    rules = new ArrayList<Rule>();
  }

  private SimpleReducer(List<Rule> rules) {
    this.rules = new ArrayList<Rule>(rules);
  }

  public ReducerBuilder builder() {
    return new Builder(rules);
  }

  private Value reduceChildren(Value value, MapBinding binding) {
    if(!value.isAtomic()) {
      Value[] vs = new Value[value.size()];
      for(int i = 0; i < value.size(); i++) {
        vs[i] = reduce(value.get(i));
      }
      return new Node(vs);
    } else {
      return value;
    }
  }

  private Value step(Value value, MapBinding binding) {
    value = reduceChildren(value, binding);
    for(Rule r : rules) {
      if(r.match(value, binding)) {
        Value ret = r.replace(binding);
        binding.clear();
        return ret;
      }
    }
    return value;
  }

  public Value reduce(Value value) {
    MapBinding binding = new MapBinding();
    Value v2 = value;
    do {
      value = v2;
      v2 = step(value, binding);
    } while(!v2.equals(value));
    return v2;
  }

  public static ReducerBuilder newBuilder() {
    return new Builder();
  }

  public static class Builder implements ReducerBuilder {

    private final List<Rule> rules;

    public Builder() {
      rules = new ArrayList<Rule>();
    }

    private Builder(List<Rule> rules) {
      this.rules = new ArrayList<Rule>(rules);
    }

    public void add(Rule rule) {
      rules.add(rule);
    }

    public Reducer build() {
      return new SimpleReducer(rules);
    }
  }
}