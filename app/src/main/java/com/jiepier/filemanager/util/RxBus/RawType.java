package com.jiepier.filemanager.util.RxBus;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
/**
 * Created by JiePier on 16/12/20.
 */

public class RawType {
    public static <T> Class<T> getRawType(Callback<T> callback) {
        Type genericSuperclass = callback.getClass()
                .getGenericSuperclass();
        Type[] params = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        Type type = params[0];
        return (Class<T>) getRawType(type);
    }

    private static Class<?> getRawType(Type type) {
        if (type == null) throw new NullPointerException("type == null");

        if (type instanceof Class<?>) {
            // Type is a normal class.
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
            // suspects some pathological case related to nested classes exists.
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class)) throw new IllegalArgumentException();
            return (Class<?>) rawType;
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0)
                    .getClass();
        }
        if (type instanceof TypeVariable) {
            // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
            // type that's more general than necessary is okay.
            return Object.class;
        }
        if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        }

        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or " + "GenericArrayType, but <" +
                type + "> is of type " + type.getClass()
                .getName());
    }
}