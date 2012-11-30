package pattern.tester;

public class Expect {

  public static void expect(boolean b) {
    if(!b) {
      throw new ExpectedException("expected");
    }
  }

  public static <T> void expectSame(T a, T b) {
    if(a != b) {
      throw new ExpectedException("expected same:\n  " + String.valueOf(a) + "\n  " + String.valueOf(b));
    }
  }

  public static <T> void expectDifferent(T a, T b) {
    if(a == b) {
      throw new ExpectedException("expected different:\n  " + String.valueOf(a) + "\n  " + String.valueOf(b));
    }
  }

  public static <T> void expectEqual(T a, T b) {
    if((a == null && b != null) || !a.equals(b)) {
      throw new ExpectedException("expected equal:\n  " + String.valueOf(a) + "\n  " + String.valueOf(b));
    } 
  }

  public static <T> void expectNotEqual(T a, T b) {
    if((a == null && b == null) || a.equals(b)) {
      throw new ExpectedException("expected not equal:\n  " + String.valueOf(a) + "\n  " + String.valueOf(b));
    }
  }
  
}