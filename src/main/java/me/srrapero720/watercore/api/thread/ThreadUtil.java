package me.srrapero720.watercore.api.thread;

import me.srrapero720.watercore.internal.WConsole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ThreadUtil {
    private static final Thread.UncaughtExceptionHandler EXCEPTION_HANDLER = (t, e) ->
            WConsole.error(t.getName(), "Handled fatal exception occurred on ThreadUtils - " + e);

    public static <T> T tryAndReturn(ReturnableRunnable<T> runnable, T defaultVar) {
        try { return runnable.run(defaultVar);
        } catch (Exception ignored) { return defaultVar; }
    }

    public static void trySimple(SimpleTryRunnable runnable) {
        try { runnable.run(); } catch (Exception ignored) {}
    }

    public static void threadTry(@NotNull TryRunnable toTry, @Nullable CatchRunnable toCatch, @Nullable FinallyRunnable toFinally) {
        threadTryArgument(null, (object -> toTry.run()), toCatch, (object -> { if (toFinally != null) toFinally.run(); }));
    }

    public static void thread(Runnable runnable) {
        var thread = new Thread(runnable);
        thread.setName("WATERCoRE-" + String.valueOf(Math.random() * 100).replace(".", "-"));
        thread.setDaemon(true);
        thread.setUncaughtExceptionHandler(EXCEPTION_HANDLER);
        thread.start();
    }

    public static <T> void threadTryArgument(T object, TryRunnableWithArgument<T> toTry, @Nullable CatchRunnable toCatch, @Nullable FinallyRunnableWithArgument<T> toFinally) {
        thread(() -> {
            try { toTry.run(object);
            } catch (Exception e) { if (toCatch != null) toCatch.run(e);
            } finally { if (toFinally != null) toFinally.run(object); }
        });
    }

    public interface ReturnableRunnable<T> { T run(T defaultVar) throws Exception; }
    public interface SimpleTryRunnable { void run() throws Exception; }

    public interface TryRunnableWithArgument<T> {  void run(T object) throws Exception; }
    public interface FinallyRunnableWithArgument<T> { void run(T object); }

    public interface TryRunnable {  void run() throws Exception; }
    public interface CatchRunnable {  void run(Exception e); }
    public interface FinallyRunnable { void run(); }
}
