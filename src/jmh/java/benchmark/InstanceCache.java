package benchmark;

import org.jetbrains.annotations.NotNull;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

@State(Scope.Benchmark)
@Threads(1)
public class InstanceCache {
  private final @NotNull Map<Container, Container> unsafe = new HashMap<>();
  private final @NotNull Map<Container, Container> threadSafe = new ConcurrentHashMap<>();
  private final @NotNull Map<Container, WeakReference<Container>> weak = new WeakHashMap<>();
  private final @NotNull Map<Container, WeakReference<Container>> synchronizedWeak = Collections.synchronizedMap(new WeakHashMap<>());

  @Setup
  public void setUp() {
    Container container = createContainer();
    unsafe.put(container, container);
    threadSafe.put(container, container);
    weak.put(container, new WeakReference<>(container));
    synchronizedWeak.put(container, new WeakReference<>(container));
  }

  @Benchmark
  public Container noCache() {
    return createContainer();
  }

  @Benchmark
  public Container unsafe() {
    Container container = createContainer();
    return Objects.requireNonNullElse(unsafe.putIfAbsent(container, container), container);
  }

  @Benchmark
  public Container threadSafe() {
    Container container = createContainer();
    return Objects.requireNonNullElse(threadSafe.putIfAbsent(container, container), container);
  }

  @Benchmark
  public Container weak() {
    Container container = createContainer();
    WeakReference<Container> ref = new WeakReference<>(container);
    Container result = null;
    while (result == null) {
      result = Objects.requireNonNullElse(weak.putIfAbsent(container, ref), ref).get();
    }
    return result;
  }

  @Benchmark
  public Container synchronizedWeak() {
    Container container = createContainer();
    WeakReference<Container> ref = new WeakReference<>(container);
    Container result = null;
    while (result == null) {
      result = Objects.requireNonNullElse(synchronizedWeak.putIfAbsent(container, ref), ref).get();
    }
    return result;
  }

  @Benchmark
  @Threads(2)
  public Container contendedWeak() {
    Container container = createContainer();
    WeakReference<Container> ref = new WeakReference<>(container);
    Container result = null;
    while (result == null) {
      result = Objects.requireNonNullElse(synchronizedWeak.putIfAbsent(container, ref), ref).get();
    }
    return result;
  }

  private @NotNull Container createContainer() {
    return new Container(42L, 9999L);
  }

  private static final class Container {
    private final long first;
    private final long second;

    private Container(long first, long second) {
      this.first = first;
      this.second = second;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Container container = (Container) o;
      return first == container.first && second == container.second;
    }

    @Override
    public int hashCode() {
      return Objects.hash(first, second);
    }
  }
}
