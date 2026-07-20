package com.pe.advanced.domain.utils;

import java.util.Objects;

public class ClassUtils {

    private ClassUtils() {
        // Static helpers
    }

    /**
     * Knows how to find the element of an enumeration given by its class and the name of the element.
     * <p/>
     * The method is useful when we restore an enumeration element given by the name of the enumeration and the name of
     * the element as the Class.forName(:String) will return Class&lt;?&gt; instance.
     * <p/>
     * This method can handle the situation when the supplied <code>enumClass</code> is the class of a specialized enum
     * constant.
     *
     * @param enumClass       - the erased class of the enumeration.
     * @param enumElementName - the name of the enum element
     * @return an enum
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> Enum<T> enumForName(Class<? extends T> enumClass, String enumElementName) {
        Objects.requireNonNull(enumClass, "class not specified");
        Objects.requireNonNull(enumElementName, "enum element name not specified");

        if (!isEnum(enumClass)) {
            throw new IllegalArgumentException(enumClass.getName() + " is not enum");
        }
        Class<T> c = (Class<T>) (isSpecialisedEnumConstant(enumClass) ? enumClass.getSuperclass() : enumClass);
        return Enum.valueOf(c, enumElementName);
    }

    /**
     * Tells if a class is enumeration or specialized enum constant. Specialized enum constants are implemented by the
     * JVM as enum inner classes, extending the enum. However their ENUM flag bit is not set, and {@link Class#isEnum()}
     * returns false. Clients of this method are JSF convertors that require a clear indication if an object can be
     * converted to enum constant, no matter if the class of the object is pure enum, or specialized enum constant inner
     * class.
     *
     * @param clazz - the class to test
     * @return true if the class represents an enumeration
     */
    public static boolean isEnum(Class<?> clazz) {
        Objects.requireNonNull(clazz, "the class can not be null");
        return Enum.class.isAssignableFrom(clazz);
    }

    /**
     * Tells if the given class is specialized enum constant. JVM represents it as inner class of the enum and
     * Class.isEnum() returns false
     *
     * @param clazz the given class
     * @return true if it's enum constant, false otherwise
     */
    public static boolean isSpecialisedEnumConstant(Class<?> clazz) {
        return isEnum(clazz) && !clazz.isEnum();
    }
}
