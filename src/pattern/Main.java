package pattern;

public class Main {

  public static void main(String[] args) {
    Namespace ns = new Namespace();
    Symbol a = ns.symbol("a");
    Symbol b = ns.symbol("b");
    System.out.println(new Node(a, b));
    Variable Variable = new Variable("_");
    Value rec = new Recursive(Variable, new Node(a, Variable), b, 2);
    System.out.println(rec);
    System.out.println(rec.format());
    System.out.println(rec.fullFormat());

    Graph g = new Graph();
    g.putReduction(new Pattern(a), new Pattern(b));
    System.out.println(g.reduce(a));
  }
}