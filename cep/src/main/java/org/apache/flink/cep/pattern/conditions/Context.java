package org.apache.flink.cep.pattern.conditions;

/**
 * The context used when evaluating the {@link IterativeCondition condition}.
 */
public interface Context<T> {

    /**
     * @param name The name of the pattern.
     * @return An {@link Iterable} over the already accepted elements
     * for a given pattern. Elements are iterated in the order they were
     * inserted in the pattern.
     */
    Iterable<T> getEventsForPattern(String name) throws Exception;
}