package benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.IntStream;

@State(Scope.Benchmark)
public class EnhancedForHelper {

  private String[] strings = IntStream.range(0, 1000)
      .mapToObj(index -> "String " + index)
      .toArray(String[]::new);

  @Benchmark
  public int fori() {
    int result = 0;
    for (int i = 0; i < strings.length; i++) {
      result ^= merge(i, strings[i]);
    }
    return result;
  }

  @Benchmark
  public int enhanced_for() {
    int result = 0;
    for (ListIndex<String> item : enumerate(strings)) {
      result ^= merge(item.index(), item.element());
    }
    return result;
  }

  /* JDK20
  @Benchmark
  public int enhanced_for_with_pattern_matching() {
    int result = 0;
    for (ListIndex(int index, String element) : enumerate(strings)) {
      result ^= merge(index, element);
    }
    return result;
  }
  //JDK20 */

  private int merge(int index, String value) {
    return 7 * index ^ value.hashCode();
  }

  record ListIndex<T>(int index, T element) {}

  static <T> Iterable<ListIndex<T>> enumerate(T[] array) {
    return enumerate(Arrays.asList(array));
  }

  static <T> Iterable<ListIndex<T>> enumerate(Iterable<T> iterable) {
    return () -> new Iterator<>() {
      private final Iterator<T> iterator = iterable.iterator();
      private int nextIndex;

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public ListIndex<T> next() {
        return new ListIndex<>(nextIndex++, iterator.next());
      }
    };
  }

}
