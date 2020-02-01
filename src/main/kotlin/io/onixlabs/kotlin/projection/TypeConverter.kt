package io.onixlabs.kotlin.projection

/**
 * Represents the base class for implementing type converters.
 *
 * @param T The underlying type that the type converter will convert to.
 */
abstract class TypeConverter<T> {

    /**
     * Converts the specified value to the [T] type.
     *
     * @param value The value to convert.
     * @return Returns a [T] representation of the specified value.
     */
    abstract fun convert(value: Any): T
}