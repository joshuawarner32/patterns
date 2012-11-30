package pattern.tester;

public class Expect {

  public static <T> void expectSame(T a, T b) {
    if(a != b) {
      throw new ExpectedException("expected same:\n  " + a.toString() + "\n  " + b.toString());
    }
  }

  public static <T> void expectDifferent(T a, T b) {
    if(a == b) {
      throw new ExpectedException("expected different:\n  " + a.toString() + "\n  " + b.toString());
    }
  }

  public static <T> void expectEqual(T a, T b) {
    if(!a.equals(b)) {
      throw new ExpectedException("expected equal:\n  " + a.toString() + "\n  " + b.toString());
    } 
  }

  public static <T> void expectNotEqual(T a, T b) {
    if(a.equals(b)) {
      throw new ExpectedException("expected not equal:\n  " + a.toString() + "\n  " + b.toString());
    }
  }
  
}