package xyz.brassgoggledcoders.skyships.util.functional;

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
    }
}
