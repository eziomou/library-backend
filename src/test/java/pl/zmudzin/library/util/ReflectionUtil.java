package pl.zmudzin.library.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Piotr Żmudzin
 */
public class ReflectionUtil {

    public static <T> T getInstance(Class<T> clazz) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    public static void setField(Class<?> clazz, Object target, String name, Object value) {
        while (clazz != Object.class) {
            try {
                Field field = clazz.getDeclaredField(name);
                try {
                    field.setAccessible(true);
                    field.set(target, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
    }

    public static void setField(Object target, String name, Object value) {
        setField(target.getClass(), target, name, value);
    }
}
