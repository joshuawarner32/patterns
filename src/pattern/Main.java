package pattern;

import pattern.reducer.Reducer;
import pattern.reducer.simple.SimpleReducer;

public class Main {

  private static Reducer reducer = new SimpleReducer();

  public static void main(String[] args) {

    Namespace ns = new Namespace();

    LineReader reader = new LineReader();
    while(true) {
      String line = reader.readLine();
      try {
        Value value = Parser.parse(ns, line);
        Value res = reducer.reduce(value);
        System.out.println(res.toString());

      } catch(ParseException e) {
        System.out.println("parse exception: " + e.getMessage());
      }
    }
    
  }
}