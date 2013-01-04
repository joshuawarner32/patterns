package pattern.reducer.graph;

import java.util.Collections;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class PersistentGraph<N, E> {

  private final Map<N, Map<E, N>> forwardEdges;
  private final Map<N, Map<E, N>> backwardEdges;

  public PersistentGraph() {
    forwardEdges = new HashMap<N, Map<E, N>>();
    backwardEdges = new HashMap<N, Map<E, N>>();
  }

  private PersistentGraph(Builder<N, E> b) {
    forwardEdges = new HashMap<N, Map<E, N>>(b.forwardEdges);
    backwardEdges = new HashMap<N, Map<E, N>>(b.backwardEdges);
  }

  public static class Builder<N, E> {
    private final Map<N, Map<E, N>> forwardEdges;
    private final Map<N, Map<E, N>> backwardEdges;

    private Builder(PersistentGraph<N, E> g) {
      forwardEdges = new HashMap<N, Map<E, N>>(g.forwardEdges);
      backwardEdges = new HashMap<N, Map<E, N>>(g.backwardEdges);
    }

    private Builder() {
      forwardEdges = new HashMap<N, Map<E, N>>();
      backwardEdges = new HashMap<N, Map<E, N>>();
    }

    public PersistentGraph<N, E> build() {
      return new PersistentGraph<N, E>(this);
    }

    private static <N, E> void put(Map<N, Map<E, N>> map, N src, E edge, N target) {
      Map<E, N> m = map.get(src);
      if(m == null) {
        map.put(src, m = new HashMap<E, N>());
      }
      m.put(edge, target);
    }

    public void add(N src, E edge, N target) {
      put(forwardEdges, src, edge, target);
      put(backwardEdges, target, edge, src);
    }
  }

  private static <N, E> N get(Map<N, Map<E, N>> map, N src, E edge) {
    Map<E, N> m = map.get(src);
    if(m == null) {
      return null;
    }
    return m.get(edge);
  }

  public Builder<N, E> builder() {
    return new Builder<N, E>(this);
  }

  public static <N, E> Builder<N, E> newBuilder() {
    return new Builder<N, E>();
  }

  public N getTarget(N src, E edge) {
    return get(forwardEdges, src, edge);
  }

  public N getSource(E edge, N dest) {
    return get(backwardEdges, dest, edge);
  }
  
}