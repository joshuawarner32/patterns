package pattern.graph;

class TransitionToState {
  final Transition transition;
  final State state;

  TransitionToState(Transition transition, State state) {
    this.transition = transition;
    this.state = state;
  }

}