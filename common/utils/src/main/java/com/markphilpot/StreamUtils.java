package com.markphilpot;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StreamUtils {
  private static final Logger log = LogManager.getLogger(StreamUtils.class);

  public static <T> Consumer<T> forEachWithIndex(BiConsumer<Integer, T> consumer) {
    AtomicInteger counter = new AtomicInteger(0);
    return item -> consumer.accept(counter.getAndIncrement(), item);
  }

  public record Indexed<T>(long index, T value) {
    public static <T> Indexed<T> buildIndexValue(long index, T value) {
      return new Indexed<T>(index, value);
    }
  }

  private static LongStream indices() {
    return LongStream.iterate(0L, l -> l + 1);
  }

  private static Runnable closerFor(Stream... streams) {
    return closerFor(Arrays.asList(streams));
  }

  private static <T> Runnable closerFor(List<Stream<T>> streams) {
    return () -> {
      List<Exception> exceptions = new LinkedList<>();
      for (Stream<T> stream : streams) {
        try {
          stream.close();
        } catch (Exception e) {
          exceptions.add(e);
        }
      }
      exceptions.stream()
          .reduce(
              (e1, e2) -> {
                e1.addSuppressed(e2);
                return e1;
              })
          .ifPresent(
              combined -> {
                throw new RuntimeException(combined);
              });
    };
  }

  private static <L, R, O> Stream<O> zip(
      Stream<L> lefts, Stream<R> rights, BiFunction<L, R, O> combiner) {
    return StreamSupport.stream(
            ZippingSpliterator.zipping(lefts.spliterator(), rights.spliterator(), combiner), false)
        .onClose(closerFor(lefts, rights));
  }

  public static <T> Stream<Indexed<T>> zipWithIndex(Stream<T> source) {
    return zip(indices().boxed(), source, Indexed::buildIndexValue).onClose(source::close);
  }
}
