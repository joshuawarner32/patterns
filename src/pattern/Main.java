package pattern;

public class Main {

  public static void main(String[] args) {
    Namespace ns = new Namespace();

    LineReader reader = new LineReader();
    while(true) {
      String line = reader.readLine();
      try {
        Value value = Parser.parse(ns, line);
        System.out.println(value.toString());
      } catch(ParseException e) {
        System.out.println("parse exception: " + e.getMessage());
      }
    }
    
  }
}