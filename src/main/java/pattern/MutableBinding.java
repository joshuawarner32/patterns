package pattern;

public interface MutableBinding extends Binding {
  public void bind(Variable a, Value v);
  public boolean isEmpty();
  public void clear();
}