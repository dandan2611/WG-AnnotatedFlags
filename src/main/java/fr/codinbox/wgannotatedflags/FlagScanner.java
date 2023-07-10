package fr.codinbox.wgannotatedflags;

import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * A scanner for flags.
 */
public class FlagScanner {

    /**
     * Register all flags of a class.
     *
     * @param clazz the class to scan
     * @param registry the flag registry
     * @throws FlagConflictException if a flag is already registered
     * @throws IllegalAccessException if a flag cannot be set
     */
    public static void registerFlags(@NotNull Class<?> clazz, @NotNull FlagRegistry registry) throws FlagConflictException, IllegalAccessException {
        var flagFields = scanStaticFlagFields(clazz);
        for (var field : flagFields) {
            var flag = FlagBuilder.construct(field);
            var accessibility = field.canAccess(null);
            if (flag == null)
                throw new IllegalArgumentException("Field " + field.getName() + " is not a flag");
            field.setAccessible(true);
            field.set(null, flag);
            field.setAccessible(accessibility);
            registry.register(flag);
        }
    }

    private static @NotNull List<Field> scanStaticFlagFields(@NotNull Class<?> clazz) {
        var fields = new ArrayList<Field>();
        for (var field : clazz.getFields())
            if (Modifier.isStatic(field.getModifiers()) && FlagBuilder.isRegistrableFlag(field))
                fields.add(field);
        return fields;
    }

}
