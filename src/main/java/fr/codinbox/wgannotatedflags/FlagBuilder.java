package fr.codinbox.wgannotatedflags;

import com.sk89q.worldguard.protection.flags.Flag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FlagBuilder {

    private static final Map<Class<? extends Annotation>, FlagConstructor<Annotation>> constructors = new HashMap<>();

    static {
        constructors.put(RegisterStringFlag.class,
                (annotation, field) -> new com.sk89q.worldguard.protection.flags.StringFlag(
                        ((RegisterStringFlag) annotation).name(),
                        ((RegisterStringFlag) annotation).defaultValue()
                )
        );
        constructors.put(RegisterStateFlag.class,
                (annotation, field) -> new com.sk89q.worldguard.protection.flags.StateFlag(
                        ((RegisterStateFlag) annotation).name(),
                        ((RegisterStateFlag) annotation).defaultValue()
                )
        );
        constructors.put(RegisterTimestampFlag.class,
                (annotation, field) -> new com.sk89q.worldguard.protection.flags.TimestampFlag(
                        ((RegisterTimestampFlag) annotation).name()
                )
        );
        constructors.put(RegisterLocationFlag.class,
                (annotation, field) -> new com.sk89q.worldguard.protection.flags.LocationFlag(
                        ((RegisterLocationFlag) annotation).name()
                )
        );
        constructors.put(RegisterUUIDFlag.class,
                (annotation, field) -> new com.sk89q.worldguard.protection.flags.UUIDFlag(
                        ((RegisterUUIDFlag) annotation).name()
                )
        );
        constructors.put(RegisterIntegerFlag.class,
                (annotation, field) -> new com.sk89q.worldguard.protection.flags.IntegerFlag(
                        ((RegisterIntegerFlag) annotation).name()
                )
        );
    }

    /**
     * Construct a flag from a field.
     *
     * @param field the field
     * @return the flag
     */
    public static @Nullable Flag<?> construct(@NotNull Field field) {
        var supportedAnnotationClasses = constructors.keySet();

        for (var annotation : field.getAnnotations()) {
            if (supportedAnnotationClasses.contains(annotation.annotationType())) {
                var constructor = constructors.get(annotation.annotationType());
                if (constructor == null)
                    return null;
                return constructor.construct(annotation, field);
            }
        }
        throw new IllegalArgumentException("Field " + field.getName() + " is not a flag");
    }

    /**
     * Check if a field is a registrable flag.
     *
     * @param field the field
     * @return true if the field is a registrable flag
     */
    public static boolean isRegistrableFlag(@NotNull Field field) {
        var supportedAnnotationClasses = constructors.keySet();

        for (var annotation : field.getAnnotations()) {
            if (supportedAnnotationClasses.contains(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Register a flag constructor.
     *
     * @param annotationClass the annotation class
     * @param constructor the flag constructor
     */
    public static void registerConstructor(@NotNull Class<? extends Annotation> annotationClass, @NotNull FlagConstructor<Annotation> constructor) {
        constructors.put(annotationClass, constructor);
    }

    /**
     * A flag constructor.
     *
     * @param <A> the annotation type
     */
    public interface FlagConstructor<A extends Annotation> {
        @NotNull Flag<?> construct(@NotNull A annotation, @NotNull Field field);
    }

}
