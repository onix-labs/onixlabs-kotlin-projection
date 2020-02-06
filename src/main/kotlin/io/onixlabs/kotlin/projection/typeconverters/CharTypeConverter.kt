package io.onixlabs.kotlin.projection.typeconverters

import io.onixlabs.kotlin.core.isInteger
import io.onixlabs.kotlin.core.math.isInteger
import io.onixlabs.kotlin.core.reflection.kotlinClass
import io.onixlabs.kotlin.core.toBigDecimal
import io.onixlabs.kotlin.core.toBigInteger
import io.onixlabs.kotlin.projection.IllegalTypeConversionException
import io.onixlabs.kotlin.projection.IllegalTypeConversionException.Companion.NUMERIC_LOSS_OF_PRECISION
import io.onixlabs.kotlin.projection.IllegalTypeConversionException.Companion.NUMERIC_OVERFLOW
import io.onixlabs.kotlin.projection.TypeConverter
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Provides a mechanism to safely convert the specified value value to the [Char] type.
 */
class CharTypeConverter : TypeConverter<Char>() {

    private companion object {
        const val EX_STR_TO_CHR = "Illegal type conversion. String value cannot be converted to Char."
    }

    /**
     * Converts the specified value to the [Char] type.
     *
     * @param value The value to convert.
     * @return Returns a [Char] representation of the specified value.
     */
    override fun convert(value: Any): Char = when (value) {
        is Boolean -> if (value) '1' else '0'
        is Byte -> value.toChar()
        is Short -> value.toChar()
        is Int -> value.toCharChecked()
        is Long -> value.toCharChecked()
        is BigInteger -> value.toCharChecked()
        is Float -> value.toCharChecked()
        is Double -> value.toCharChecked()
        is BigDecimal -> value.toCharChecked()
        is String -> value.toCharChecked()
        is Char -> value
        else -> throw IllegalTypeConversionException(value.kotlinClass, Char::class)
    }

    /**
     * Performs a checked conversion from [Int] to [Char].
     *
     * @return Returns a [Char] representation of the specified [Int].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Char].
     */
    private fun Int.toCharChecked(): Char = when {
        (this !in Short.MIN_VALUE..Short.MAX_VALUE) -> throw NUMERIC_OVERFLOW
        else -> toChar()
    }

    /**
     * Performs a checked conversion from [Long] to [Char].
     *
     * @return Returns a [Char] representation of the specified [Long].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Char].
     */
    private fun Long.toCharChecked(): Char = when {
        (this !in Short.MIN_VALUE..Short.MAX_VALUE) -> throw NUMERIC_OVERFLOW
        else -> toChar()
    }

    /**
     * Performs a checked conversion from [BigInteger] to [Char].
     *
     * @return Returns a [Char] representation of the specified [BigInteger].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Char].
     */
    private fun BigInteger.toCharChecked(): Char = when {
        (this !in Short.MIN_VALUE.toBigInteger()..Short.MAX_VALUE.toBigInteger()) -> throw NUMERIC_OVERFLOW
        else -> toShort().toChar()
    }

    /**
     * Performs a checked conversion from [Float] to [Char].
     *
     * @return Returns a [Char] representation of the specified [Float].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Char].
     */
    private fun Float.toCharChecked(): Char = when {
        (!isInteger()) -> throw NUMERIC_LOSS_OF_PRECISION
        (this !in Short.MIN_VALUE.toFloat()..Short.MAX_VALUE.toFloat()) -> throw NUMERIC_OVERFLOW
        else -> toChar()
    }

    /**
     * Performs a checked conversion from [Double] to [Char].
     *
     * @return Returns a [Char] representation of the specified [Double].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Char].
     */
    private fun Double.toCharChecked(): Char = when {
        (!isInteger()) -> throw NUMERIC_LOSS_OF_PRECISION
        (this !in Short.MIN_VALUE.toDouble()..Short.MAX_VALUE.toDouble()) -> throw NUMERIC_OVERFLOW
        else -> toChar()
    }

    /**
     * Performs a checked conversion from [BigDecimal] to [Char].
     *
     * @return Returns a [Char] representation of the specified [BigDecimal].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Char].
     */
    private fun BigDecimal.toCharChecked(): Char = when {
        (!isInteger()) -> throw NUMERIC_LOSS_OF_PRECISION
        (this !in Short.MIN_VALUE.toBigDecimal()..Short.MAX_VALUE.toBigDecimal()) -> throw NUMERIC_OVERFLOW
        else -> toShort().toChar()
    }

    /**
     * Performs a checked conversion from [String] to [Char].
     *
     * @return Returns a [Char] representation of the specified [BigInteger].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [String].
     */
    private fun String.toCharChecked(): Char = singleOrNull() ?: throw IllegalTypeConversionException(EX_STR_TO_CHR)
}