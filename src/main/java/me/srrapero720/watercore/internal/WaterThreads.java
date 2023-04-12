package me.srrapero720.watercore.internal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WaterThreads {
    private static final Thread.UncaughtExceptionHandler EXCEPTION_HANDLER = (t, e) ->
            WaterConsole.error(t.getName(), "Handled fatal exception occurred on WaterThreads - " + e);

    public static void runNewThread(Runnable runnable) {
        var thread = new Thread(runnable);
        thread.setName("WATERCoRE-" + String.valueOf(Math.random() * 100).replace(".", "-"));
        thread.setDaemon(true);
        thread.setUncaughtExceptionHandler(EXCEPTION_HANDLER);
        thread.start();
    }
    public static void tryInNewThread(@NotNull TryRunnable toTry, @Nullable CatchRunnable toCatch, @Nullable FinallyRunnable toFinally) {
        tryInNewThreadWithArg(null, (object -> toTry.run()), toCatch, (object -> { if (toFinally != null) toFinally.run(); }));
    }
    public static <T> void tryInNewThreadWithArg(T object, TryRunnableWithArgument<T> toTry, @Nullable CatchRunnable toCatch, @Nullable FinallyRunnableWithArgument<T> toFinally) {
        runNewThread(() -> {
            try { toTry.run(object);
            } catch (Exception e) { if (toCatch != null) toCatch.run(e);
            } finally { if (toFinally != null) toFinally.run(object); }
        });
    }

    public interface TryRunnableWithArgument<T> {  void run(T object) throws Exception; }
    public interface FinallyRunnableWithArgument<T> { void run(T object); }

    public interface TryRunnable {  void run() throws Exception; }
    public interface CatchRunnable {  void run(Exception e); }
    public interface FinallyRunnable { void run(); }
}
