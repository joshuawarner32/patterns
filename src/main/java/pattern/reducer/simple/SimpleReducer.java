package pattern.reducer.simple;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import pattern.Value;
import pattern.Variable;
import pattern.Pattern;
import pattern.Node;

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

  private static boolean match(Value pat, Value value, Map<Variable, Value> context) {
    if(pat.isAtomic()) {
      if(pat instanceof Variable) {
        Variable v = (Variable)pat;
        Value prevMatch = context.get(v);
        if(prevMatch == null) {
          context.put(v, value);
          return true;
        } else {
          return prevMatch.equals(value);
        }
      } else {
        return value.equals(pat);
      }
    } else {
      if(value.isAtomic()) {
        return false;
      } else {
        if(value.size() != pat.size()) {
          return false;
        } else {
          for(int i = 0; i < value.size(); i++) {
            if(!match(pat.get(i), value.get(i), context)) {
              return false;
            }
          }
          return true;
        }
      }
    }
  }

  private static Value replace(Value pat, Map<Variable, Value> context) {
    if(pat.isAtomic()) {
      if(pat instanceof Variable) {
        Variable v = (Variable)pat;
        Value prevMatch = context.get(v);
        if(prevMatch == null) {
          throw new IllegalStateException("variable " + v + " not defined");
        } else {
          return prevMatch;
        }
      } else {
        return pat;
      }
    } else {
      Value[] vals = new Value[pat.size()];
      for(int i = 0; i < vals.length; i++) {
        vals[i] = replace(pat.get(i), context);
      }
      return new Node(vals);
    }
  }

  private static boolean match(Pattern pat, Value value, Map<Variable, Value> context) {
    return match(pat.getNakedValue(), value, context);
  }

  private static Value replace(Pattern pat, Map<Variable, Value> context) {
    return replace(pat.getNakedValue(), context);
  }

  private Value reduceChildren(Value value, Map<Variable, Value> context) {
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

  private Value step(Value value, Map<Variable, Value> context) {
    value = reduceChildren(value, context);
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