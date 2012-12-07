package pattern;

public class Main {

  public static void main(String[] args) {
    Namespace ns = new Namespace();
    Symbol a = ns.symbol("a");
    Symbol b = ns.symbol("b");
    System.out.println(new Node(a, b));
    Variable v = new Variable("_");
    Value rec = new Recursive(v, new Node(a, v), b, 2);
    System.out.println(rec);
    System.out.println(rec.format());
    System.out.println(rec.fullFormat());
  }
}