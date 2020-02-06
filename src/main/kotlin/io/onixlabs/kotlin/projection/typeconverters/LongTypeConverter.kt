package io.onixlabs.kotlin.projection.typeconverters

import io.onixlabs.kotlin.core.ONE
import io.onixlabs.kotlin.core.ZERO
import io.onixlabs.kotlin.core.isInteger
import io.onixlabs.kotlin.core.math.isInteger
import io.onixlabs.kotlin.core.reflection.kotlinClass
import io.onixlabs.kotlin.projection.IllegalTypeConversionException
import io.onixlabs.kotlin.projection.IllegalTypeConversionException.Companion.NUMERIC_LOSS_OF_PRECISION
import io.onixlabs.kotlin.projection.IllegalTypeConversionException.Companion.NUMERIC_OVERFLOW
import io.onixlabs.kotlin.projection.TypeConverter
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Provides a mechanism to safely convert the specified value value to the [Long] type.
 */
class LongTypeConverter : TypeConverter<Long>() {

    /**
     * Converts the specified value to the [Long] type.
     *
     * @param value The value to convert.
     * @return Returns a [Long] representation of the specified value.
     */
    override fun convert(value: Any): Long = when (value) {
        is Boolean -> if (value) Long.ONE else Long.ZERO
        is Byte -> value.toLong()
        is Short -> value.toLong()
        is Int -> value.toLong()
        is Long -> value
        is BigInteger -> value.toLongChecked()
        is Float -> value.toLongChecked()
        is Double -> value.toLongChecked()
        is BigDecimal -> value.toLongChecked()
        is String -> value.toLong()
        is Char -> value.toLong()
        else -> throw IllegalTypeConversionException(value.kotlinClass, Long::class)
    }

    /**
     * Performs a checked conversion from [BigInteger] to [Long].
     *
     * @return Returns a [Long] representation of the specified [BigInteger].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Long].
     */
    private fun BigInteger.toLongChecked(): Long = when {
        (this !in Long.MIN_VALUE.toBigInteger()..Long.MAX_VALUE.toBigInteger()) -> throw NUMERIC_OVERFLOW
        else -> toLong()
    }

    /**
     * Performs a checked conversion from [Float] to [Long].
     *
     * @return Returns a [Long] representation of the specified [Float].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Long].
     */
    private fun Float.toLongChecked(): Long = when {
        (!isInteger()) -> throw NUMERIC_LOSS_OF_PRECISION
        (this !in Long.MIN_VALUE.toFloat()..Long.MAX_VALUE.toFloat()) -> throw NUMERIC_OVERFLOW
        else -> toLong()
    }

    /**
     * Performs a checked conversion from [Double] to [Long].
     *
     * @return Returns a [Long] representation of the specified [Double].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Long].
     */
    private fun Double.toLongChecked(): Long = when {
        (!isInteger()) -> throw NUMERIC_LOSS_OF_PRECISION
        (this !in Long.MIN_VALUE.toDouble()..Long.MAX_VALUE.toDouble()) -> throw NUMERIC_OVERFLOW
        else -> toLong()
    }

    /**
     * Performs a checked conversion from [BigDecimal] to [Long].
     *
     * @return Returns a [Long] representation of the specified [BigDecimal].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Long].
     */
    private fun BigDecimal.toLongChecked(): Long = when {
        (!isInteger()) -> throw NUMERIC_LOSS_OF_PRECISION
        (this !in Long.MIN_VALUE.toBigDecimal()..Long.MAX_VALUE.toBigDecimal()) -> throw NUMERIC_OVERFLOW
        else -> toLong()
    }
}