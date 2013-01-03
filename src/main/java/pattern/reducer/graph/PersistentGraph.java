package pattern.reducer.graph;

import java.util.Collections;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import com.google.common.collect.SetMultimap;
import com.google.common.collect.HashMultimap;

public class PersistentGraph<N, E> {

  private final Map<N, SetMultimap<E, N>> forwardEdges;
  private final Map<N, SetMultimap<E, N>> backwardEdges;

  public PersistentGraph() {
    forwardEdges = new HashMap<N, SetMultimap<E, N>>();
    backwardEdges = new HashMap<N, SetMultimap<E, N>>();
  }

  private PersistentGraph(Builder<N, E> b) {
    forwardEdges = new HashMap<N, SetMultimap<E, N>>(b.forwardEdges);
    backwardEdges = new HashMap<N, SetMultimap<E, N>>(b.backwardEdges);
  }

  public static class Builder<N, E> {
    private final Map<N, SetMultimap<E, N>> forwardEdges;
    private final Map<N, SetMultimap<E, N>> backwardEdges;

    private Builder(PersistentGraph<N, E> g) {
      forwardEdges = new HashMap<N, SetMultimap<E, N>>(g.forwardEdges);
      backwardEdges = new HashMap<N, SetMultimap<E, N>>(g.backwardEdges);
    }

    private Builder() {
      forwardEdges = new HashMap<N, SetMultimap<E, N>>();
      backwardEdges = new HashMap<N, SetMultimap<E, N>>();
    }

    public PersistentGraph<N, E> build() {
      return new PersistentGraph<N, E>(this);
    }

    private static <N, E> Set<N> getForUpdate(Map<N, SetMultimap<E, N>> map, N src, E edge) {
      SetMultimap<E, N> m = map.get(src);
      if(m == null) {
        map.put(src, m = HashMultimap.create());
      }
      return m.get(edge);
    }

    private static <N, E> void put(Map<N, SetMultimap<E, N>> map, N src, E edge, N target) {
      Set<N> s = getForUpdate(map, src, edge);
      s.add(target);
    }

    private static <N, E> void remove(Map<N, SetMultimap<E, N>> map, N src, E edge, N target) {
      Set<N> s = getForUpdate(map, src, edge);
      s.remove(target);
      if(s.isEmpty()) {
        map.remove(src);
      }
    }

    public void add(N src, E edge, N target) {
      put(forwardEdges, src, edge, target);
      put(backwardEdges, target, edge, src);
    }

    public void remove(N src, E edge, N target) {
      remove(forwardEdges, src, edge, target);
      remove(backwardEdges, target, edge, src);
    }
  }

  private static <N, E> Set<N> get(Map<N, SetMultimap<E, N>> map, N src, E edge) {
    SetMultimap<E, N> m = map.get(src);
    if(m == null) {
      return Collections.emptySet();
    }
    return m.get(edge);
  }

  public Builder<N, E> builder() {
    return new Builder<N, E>(this);
  }

  public static <N, E> Builder<N, E> newBuilder() {
    return new Builder<N, E>();
  }

  public Set<N> getTargets(N src, E edge) {
    return Collections.unmodifiableSet(get(forwardEdges, src, edge));
  }

  public Set<N> getSources(E edge, N dest) {
    return Collections.unmodifiableSet(get(backwardEdges, dest, edge));
  }

  private static <T> T maybeFirst(Iterable<T> c) {
    Iterator<T> it = c.iterator();
    if(it.hasNext()) {
      return it.next();
    } else {
      return null;
    }
  }

  public N getAnyTarget(N src, E edge) {
    return maybeFirst(get(forwardEdges, src, edge));
  }

  public N getAnySource(E edge, N dest) {
    return maybeFirst(get(backwardEdges, dest, edge));
  }
  
}