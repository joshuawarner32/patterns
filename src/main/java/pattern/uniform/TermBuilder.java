package pattern.uniform;

import java.util.List;
import java.util.ArrayList;

public class TermBuilder {

  private Symbol head;
  public List<Term> terms = new ArrayList<Term>();

  public TermBuilder(Symbol head) {
    this.head = head;
  }

  public void append(Term t) {
    terms.add(t);
  }

  public Term toTerm() {
    return Term.makeWithoutReallocate(head, terms.toArray(new Term[terms.size()]));
  }
}