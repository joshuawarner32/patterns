package pattern.reducer.simple;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import pattern.Value;
import pattern.Variable;
import pattern.Pattern;

import pattern.reducer.Rule;
import pattern.reducer.Reducer;
import pattern.reducer.ReducerBuilder;

public class SimpleReducer implements Reducer {

  private List<Rule> rules;

  private SimpleReducer(List<Rule> rules) {
    this.rules = new ArrayList<Rule>(rules);
  }

  public ReducerBuilder builder() {
    return new Builder(rules);
  }

  private static boolean match(Pattern pat, Value value, Map<Variable, Value> context) {
    return value == pat.getNakedValue();
  }

  private static Value replace(Pattern pat, Map<Variable, Value> context) {
    return pat.getNakedValue();
  }

  private Value step(Value value, Map<Variable, Value> context) {
    for(Rule r : rules) {
      if(match(r.match, value, context)) {
        Value ret = replace(r.replace, context);
        context.clear();
        return ret;
      }
    }
    return value;
  }

  public Value reduce(Value value) {
    Map<Variable, Value> context = new HashMap<Variable, Value>();
    Value v2 = value;
    do {
      value = v2;
      v2 = step(value, context);
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