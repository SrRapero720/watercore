package me.srrapero720.watercore.api.thread;

import com.google.common.annotations.Beta;
import me.srrapero720.watercore.api.thread.decorators.Threaded;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;


@Beta
public class MultiThreadHandler {
    @SuppressWarnings("unchecked")
    public static <T> @NotNull T createCompletableFuture(@NotNull T target) {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), (proxy, method, args) -> {
            if (method.isAnnotationPresent(Threaded.class)) {
                return CompletableFuture.runAsync(() -> {
                    try {

                        method.invoke(target, args);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            return method.invoke(target, args);
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull T createThreaded(@NotNull T target) {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), (proxy, method, args) -> {
            if (method.isAnnotationPresent(Threaded.class)) {
                return CompletableFuture.runAsync(() -> {
                    try {
                        method.invoke(target, args);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            return method.invoke(target, args);
        });
    }
}
