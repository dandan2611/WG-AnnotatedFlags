package fr.codinbox.wgannotatedflags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Register a {@link com.sk89q.worldguard.protection.flags.StateFlag}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RegisterStateFlag {

    /**
     * The name of the flag
     *
     * @return the name of the flag
     */
    String name();

    /**
     * The default value of the flag
     *
     * @return the default value of the flag
     */
    boolean defaultValue() default false;

}
