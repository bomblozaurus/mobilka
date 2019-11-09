package teamE.common;

@FunctionalInterface
public interface Transformer<T, R> {
    R transform(T object);
}

