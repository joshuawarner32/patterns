package pattern.reducer;

import java.io.IOException;
import java.io.InputStream;

public class ResourceReducerTest {

  private static String loadResourceAsString(String path) {
    InputStream stream = ResourceReducerTest.class.getClassLoader().getResourceAsStream(path);
    try {
      StringBuilder res = new StringBuilder();
      byte[] buf = new byte[1024];
      int size;
      while((size = stream.read(buf, 0, buf.length)) > 0) {
        res.append(new String(buf, 0, size));
      }
      return res.toString();
    } catch(IOException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        stream.close();
      } catch(IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

}