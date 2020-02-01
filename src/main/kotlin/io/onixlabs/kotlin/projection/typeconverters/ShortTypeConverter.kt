package io.onixlabs.kotlin.projection.typeconverters

import io.onixlabs.kotlin.core.*
import io.onixlabs.kotlin.core.math.isInteger
import io.onixlabs.kotlin.core.reflection.kotlinClass
import io.onixlabs.kotlin.projection.IllegalTypeConversionException
import io.onixlabs.kotlin.projection.IllegalTypeConversionException.Companion.NUMERIC_LOSS_OF_PRECISION
import io.onixlabs.kotlin.projection.IllegalTypeConversionException.Companion.NUMERIC_OVERFLOW
import io.onixlabs.kotlin.projection.TypeConverter
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Provides a mechanism to safely convert the specified value value to the [Short] type.
 */
class ShortTypeConverter : TypeConverter<Short>() {

    /**
     * Converts the specified value to the [Short] type.
     *
     * @param value The value to convert.
     * @return Returns a [Short] representation of the specified value.
     */
    override fun convert(value: Any): Short = when (value) {
        is Boolean -> if (value) Short.ONE else Short.ZERO
        is Byte -> value.toShort()
        is Short -> value
        is Int -> value.toShortChecked()
        is Long -> value.toShortChecked()
        is BigInteger -> value.toShortChecked()
        is Float -> value.toShortChecked()
        is Double -> value.toShortChecked()
        is BigDecimal -> value.toShortChecked()
        is String -> value.toShort()
        is Char -> value.toShort()
        else -> throw IllegalTypeConversionException(value.kotlinClass, Short::class)
    }

    /**
     * Performs a checked conversion from [Int] to [Short].
     *
     * @return Returns a [Short] representation of the specified [Int].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Short].
     */
    private fun Int.toShortChecked(): Short = when {
        (this !in Short.MIN_VALUE..Short.MAX_VALUE) -> throw NUMERIC_OVERFLOW
        else -> toShort()
    }

    /**
     * Performs a checked conversion from [Long] to [Short].
     *
     * @return Returns a [Short] representation of the specified [Long].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Short].
     */
    private fun Long.toShortChecked(): Short = when {
        (this !in Short.MIN_VALUE..Short.MAX_VALUE) -> throw NUMERIC_OVERFLOW
        else -> toShort()
    }

    /**
     * Performs a checked conversion from [BigInteger] to [Short].
     *
     * @return Returns a [Short] representation of the specified [BigInteger].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Short].
     */
    private fun BigInteger.toShortChecked(): Short = when {
        (this !in Short.MIN_VALUE.toBigInteger()..Short.MAX_VALUE.toBigInteger()) -> throw NUMERIC_OVERFLOW
        else -> toShort()
    }

    /**
     * Performs a checked conversion from [Float] to [Short].
     *
     * @return Returns a [Short] representation of the specified [Float].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Short].
     */
    private fun Float.toShortChecked(): Short = when {
        (!isInteger()) -> throw NUMERIC_LOSS_OF_PRECISION
        (this !in Short.MIN_VALUE..Short.MAX_VALUE) -> throw NUMERIC_OVERFLOW
        else -> toShort()
    }

    /**
     * Performs a checked conversion from [Double] to [Short].
     *
     * @return Returns a [Short] representation of the specified [Double].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Short].
     */
    private fun Double.toShortChecked(): Short = when {
        (!isInteger()) -> throw NUMERIC_LOSS_OF_PRECISION
        (this !in Short.MIN_VALUE..Short.MAX_VALUE) -> throw NUMERIC_OVERFLOW
        else -> toShort()
    }

    /**
     * Performs a checked conversion from [BigDecimal] to [Short].
     *
     * @return Returns a [Short] representation of the specified [BigDecimal].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Short].
     */
    private fun BigDecimal.toShortChecked(): Short = when {
        (!isInteger()) -> throw NUMERIC_LOSS_OF_PRECISION
        (this !in Short.MIN_VALUE.toBigDecimal()..Short.MAX_VALUE.toBigDecimal()) -> throw NUMERIC_OVERFLOW
        else -> toShort()
    }
}