package pattern.graph;

import static pattern.tester.Expect.*;

import pattern.Namespace;
import pattern.Symbol;
import pattern.Variable;

public class GraphTest {

  Namespace ns = new Namespace();
  Variable rootVariable = new Variable("_");
  Symbol sym = ns.symbol("sym");
  Symbol sym1 = ns.symbol("sym1");
  Symbol sym2 = ns.symbol("sym2");

  public void testTrivialReduceTo() {
    State a = new State();
    State b = new State();

    expect(!a.isLive());
    expect(!b.isLive());
    a.reduceTo(b);
    expect(a.isLive());
    expect(!b.isLive());
    throw new RuntimeException("test");
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

  public void testSymmetricBlazeState() {
    Variable v1 = new Variable("v1");
    Variable v2 = new Variable("v2");

    State a = new State();

    Transition t1 = new SymbolTransition(v1, sym1);
    Transition t2 = new SymbolTransition(v2, sym2);

    State b1 = a.blazeState(t1);

    State b2 = a.blazeState(t2);

    State c = b1.blazeState(t2);

    b2.putTransition(t1, c);

    expectEqual(a.getTransition(t1), b1);
    expectEqual(a.getTransition(t2), b2);
    expectEqual(b1.getTransition(t2), c);
    expectEqual(b1.getTransition(t1), null);
    expectEqual(b2.getTransition(t1), c);
    expectEqual(b1.getTransition(t1), null);
  }

}