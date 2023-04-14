package me.srrapero720.watercore.api.thread.decorators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Threaded {
    String name() default "WATERCoRE Thread";
    int priority() default -1;
    boolean daemon() default true;
}
