package pattern;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Pattern {

  private Pattern() {}

  public static boolean alreadyMatches(Value pat, Value value, Binding binding) {
    if(pat.isAtomic()) {
      if(pat instanceof Variable) {
        Variable v = (Variable)pat;
        Value prevMatch = binding.get(v);
        if(prevMatch == null) {
          return false;
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
            if(!alreadyMatches(pat.get(i), value.get(i), binding)) {
              return false;
            }
          }
          return true;
        }
      }
    }
  }

  public static boolean match(Value pat, Value value, MutableBinding binding) {
    if(pat.isAtomic()) {
      if(pat instanceof Variable) {
        Variable v = (Variable)pat;
        Value prevMatch = binding.get(v);
        if(prevMatch == null) {
          binding.bind(v, value);
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
            if(!match(pat.get(i), value.get(i), binding)) {
              return false;
            }
          }
          return true;
        }
      }
    }
  }

  public static Value replace(Value pat, Binding binding) {
    if(pat.isAtomic()) {
      if(pat instanceof Variable) {
        Variable v = (Variable)pat;
        Value prevMatch = binding.get(v);
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
        vals[i] = replace(pat.get(i), binding);
      }
      return new Node(vals);
    }
  }
}