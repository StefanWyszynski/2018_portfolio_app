package com.simple.sqldatabase.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(value = RUNTIME)
@Target(value = FIELD)
public @interface DBColumn {
    boolean primaryKey() default false;

    boolean autoIncrement() default false;
}
