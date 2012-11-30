package pattern.graph;

import static pattern.tester.Expect.*;

import pattern.Namespace;
import pattern.Symbol;
import pattern.Variable;

public class GraphTest {

  Namespace ns = new Namespace();
  Variable rootVariable = new Variable("_");
    Symbol sym = ns.symbol("sym");

  public void testTrivialReduceTo() {
    State a = new State();
    State b = new State();

    expect(!a.isLive());
    expect(!b.isLive());
    a.reduceTo(b);
    expect(a.isLive());
    expect(!b.isLive());
  }

  public void testPutTransition() {
    TransitionSet a = new TransitionSet();
    State b = new State();

    Transition t = new SymbolTransition(rootVariable, sym);

    a.putTransition(t, b);
    expectEqual(a.getTransition(t), b);
  }

  public void testSimpleBlazeState() {

    State a = new State();

    Transition t = new SymbolTransition(rootVariable, sym);

    State b = a.blazeState(t);

    expectEqual(a.getTransition(t), b);
    expectEqual(b.getTransitionFrom(t), a);
  }

}