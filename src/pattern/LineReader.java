package pattern;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class LineReader {

  private InputStreamReader converter = new InputStreamReader(System.in);
  private BufferedReader in = new BufferedReader(converter);


  public String readLine() {
    try {
      return in.readLine();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
}