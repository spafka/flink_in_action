package org.apache.flink.contrib.tensorflow.util;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An in-memory sink for test purposes.
 * <p>
 * Collects the output from a streaming program for inspection.
 * <p>
 * ```
 * val collected = ListBuffer[Float]()
 * MemorySinkFunction.registerCollection(0, collected.asJava)
 * outputs.addSink(new MemorySinkFunction[Float](0))
 * env.execute()
 * ```
 */
public class MemorySinkFunction<T> implements SinkFunction<T> {
	private static final long serialVersionUID = 1L;
	private static Map<Integer, Collection<?>> collections = new ConcurrentHashMap<>();
	private final int key;

	public MemorySinkFunction(int key) {
		this.key = key;
	}

	public static void registerCollection(int key, Collection<?> collection) {
		collections.put(key, collection);
	}

	public static void clear() {
		collections.clear();
	}

	@Override
	public void invoke(T value) {
		Collection<T> collection = (Collection<T>) collections.get(key);

		synchronized (collection) {
			collection.add(value);
		}
	}
}
