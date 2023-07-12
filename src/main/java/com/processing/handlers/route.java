package com.processing.handlers;

import java.lang.annotation.*;

@Target(value= ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface route {
    String uri();
    String method() default "GET";
}
