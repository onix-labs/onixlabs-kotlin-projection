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
 * Provides a mechanism to safely convert the specified value value to the [Int] type.
 */
class IntTypeConverter : TypeConverter<Int>() {

    /**
     * Converts the specified value to the [Int] type.
     *
     * @param value The value to convert.
     * @return Returns an [Int] representation of the specified value.
     */
    override fun convert(value: Any): Int = when (value) {
        is Boolean -> if (value) Int.ONE else Int.ZERO
        is Byte -> value.toInt()
        is Short -> value.toInt()
        is Int -> value
        is Long -> value.toIntChecked()
        is BigInteger -> value.toIntChecked()
        is Float -> value.toIntChecked()
        is Double -> value.toIntChecked()
        is BigDecimal -> value.toIntChecked()
        is String -> value.toInt()
        is Char -> value.toInt()
        else -> throw IllegalTypeConversionException(value.kotlinClass, Int::class)
    }

    /**
     * Performs a checked conversion from [Long] to [Int].
     *
     * @return Returns an [Int] representation of the specified [Long].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Int].
     */
    private fun Long.toIntChecked(): Int = when {
        (this !in Int.MIN_VALUE..Int.MAX_VALUE) -> throw NUMERIC_OVERFLOW
        else -> toInt()
    }

    /**
     * Performs a checked conversion from [BigInteger] to [Int].
     *
     * @return Returns an [Int] representation of the specified [BigInteger].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Int].
     */
    private fun BigInteger.toIntChecked(): Int = when {
        (this !in Int.MIN_VALUE.toBigInteger()..Int.MAX_VALUE.toBigInteger()) -> throw NUMERIC_OVERFLOW
        else -> toInt()
    }

    /**
     * Performs a checked conversion from [Float] to [Int].
     *
     * @return Returns an [Int] representation of the specified [Float].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Int].
     */
    private fun Float.toIntChecked(): Int = when {
        (!isInteger()) -> throw NUMERIC_LOSS_OF_PRECISION
        (this !in Int.MIN_VALUE..Int.MAX_VALUE) -> throw NUMERIC_OVERFLOW
        else -> toInt()
    }

    /**
     * Performs a checked conversion from [Double] to [Int].
     *
     * @return Returns an [Int] representation of the specified [Double].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Int].
     */
    private fun Double.toIntChecked(): Int = when {
        (!isInteger()) -> throw NUMERIC_LOSS_OF_PRECISION
        (this !in Int.MIN_VALUE..Int.MAX_VALUE) -> throw NUMERIC_OVERFLOW
        else -> toInt()
    }

    /**
     * Performs a checked conversion from [BigDecimal] to [Int].
     *
     * @return Returns an [Int] representation of the specified [BigDecimal].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Int].
     */
    private fun BigDecimal.toIntChecked(): Int = when {
        (!isInteger()) -> throw NUMERIC_LOSS_OF_PRECISION
        (this !in Int.MIN_VALUE.toBigDecimal()..Int.MAX_VALUE.toBigDecimal()) -> throw NUMERIC_OVERFLOW
        else -> toInt()
    }
}