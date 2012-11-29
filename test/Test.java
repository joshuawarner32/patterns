
public class Test {


  public void main(String[] args) {
    for(Method m : Test.class.getMethods()) {
      if(m.getName().startsWith("test")) {
        Test t = new Test();
        m.invoke(t);
      }
    }
  }
}