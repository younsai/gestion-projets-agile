package io.younsai.gestionprojetsagile.mapper;

import org.mapstruct.factory.Mappers;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public final class MapperTestUtil {
    private MapperTestUtil() {}

    @SuppressWarnings("unchecked")
    public static <T> T initMapper(Class<T> mapperClass) {
        T mapper = Mappers.getMapper(mapperClass);
        injectDependencies(mapper, new HashSet<>());
        return mapper;
    }

    private static void injectDependencies(Object mapper, Set<Class<?>> visited) {
        if (mapper == null) return;
        Class<?> cls = mapper.getClass();
        if (visited.contains(cls)) return;
        visited.add(cls);

        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            Class<?> ft = field.getType();
            if (ft.isInterface() && ft.getName().endsWith("Mapper")) {
                Object dep = Mappers.getMapper((Class) ft);
                try {
                    field.setAccessible(true);
                    field.set(mapper, dep);
                    // recursively inject into the dependency as well
                    injectDependencies(dep, visited);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
