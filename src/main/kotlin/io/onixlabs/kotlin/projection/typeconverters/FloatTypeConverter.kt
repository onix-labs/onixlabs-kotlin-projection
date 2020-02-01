package io.onixlabs.kotlin.projection.typeconverters

import io.onixlabs.kotlin.core.ONE
import io.onixlabs.kotlin.core.ZERO
import io.onixlabs.kotlin.core.reflection.kotlinClass
import io.onixlabs.kotlin.projection.IllegalTypeConversionException
import io.onixlabs.kotlin.projection.TypeConverter

/**
 * Provides a mechanism to safely convert the specified value value to the [Float] type.
 */
class FloatTypeConverter : TypeConverter<Float>() {

    /**
     * Converts an object from the input type to the output type.
     *
     * @param value The value to convert.
     * @return Returns an instance of the output type.
     */
    override fun convert(value: Any): Float = when (value) {
        is Boolean -> if (value) Float.ONE else Float.ZERO
        is Byte -> value.toFloat()
        is Short -> value.toFloat()
        is Int -> value.toFloat()
        is Float -> value
        is String -> value.toFloat()
        is Char -> value.toFloat()
        else -> throw IllegalTypeConversionException(value.kotlinClass, Float::class)
    }
}