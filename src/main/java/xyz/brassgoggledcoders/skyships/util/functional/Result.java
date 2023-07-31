package xyz.brassgoggledcoders.skyships.util.functional;

import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface Result<T> {
    Result<?> FAILURE = new Failure();
    Result<?> WAITING = new Waiting();

    boolean hasRun();

    boolean isSuccess();

    @NotNull
    T value();

    @NotNull <U> U fold(NonNullFunction<T, U> present, NonNullSupplier<U> empty);

    Result<T> or(Result<T> other);

    default Result<T> run(NonNullSupplier<Optional<T>> runner) {
        if (this.hasRun()) {
            return this;
        } else {
            return runner.get()
                    .map(Result::success)
                    .orElse(failure());
        }
    }

    @SuppressWarnings("unchecked")
    static <T> Result<T> failure() {
        return (Result<T>) FAILURE;
    }

    @SuppressWarnings("unchecked")
    static <T> Result<T> waiting() {
        return (Result<T>) WAITING;
    }

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    record Success<T>(T value) implements Result<T> {

        @Override
        public boolean hasRun() {
            return true;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        @NotNull
        public T value() {
            return this.value;
        }

        @Override
        @NotNull
        public <U> U fold(NonNullFunction<T, U> present, NonNullSupplier<U> empty) {
            return present.apply(this.value());
        }

        @Override
        public Result<T> or(Result<T> other) {
            return this;
        }
    }

    class Failure implements Result<Void> {

        @Override
        public boolean hasRun() {
            return true;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        @NotNull
        public Void value() {
            throw new IllegalStateException("Failure does not have a value");
        }

        @Override
        @NotNull
        public <U> U fold(NonNullFunction<Void, U> present, NonNullSupplier<U> empty) {
            return empty.get();
        }

        @Override
        public Result<Void> or(Result<Void> other) {
            return other;
        }
    }

    class Waiting implements Result<Void> {

        @Override
        public boolean hasRun() {
            return false;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        @NotNull
        public Void value() {
            throw new IllegalStateException("Waiting does not have a value");
        }

        @Override
        @NotNull
        public <U> U fold(NonNullFunction<Void, U> present, NonNullSupplier<U> empty) {
            return empty.get();
        }

        @Override
        public Result<Void> or(Result<Void> other) {
            return other;
        }
    }
}
