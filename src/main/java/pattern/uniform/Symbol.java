package pattern.uniform;

public class Symbol {
  public final String name;
  public final int arity;

  private final Term selfTerm;

  public Symbol(String name, int arity) {
    this.name = name;
    this.arity = arity;
    selfTerm = arity == 0 ? new Term(this) : null;
  }

  public Term term(Term... terms) {
    return new Term(this, terms);
  }

  public Term self() {
    if(arity != 0) {
      throw new IllegalArgumentException();
    }
    return selfTerm;
  }
}