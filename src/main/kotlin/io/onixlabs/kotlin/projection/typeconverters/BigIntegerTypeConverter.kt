package io.onixlabs.kotlin.projection.typeconverters

import io.onixlabs.kotlin.core.isInteger
import io.onixlabs.kotlin.core.math.isInteger
import io.onixlabs.kotlin.core.reflection.kotlinClass
import io.onixlabs.kotlin.projection.IllegalTypeConversionException
import io.onixlabs.kotlin.projection.IllegalTypeConversionException.Companion.NUMERIC_LOSS_OF_PRECISION
import io.onixlabs.kotlin.projection.TypeConverter
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Provides a mechanism to safely convert the specified value value to the [BigInteger] type.
 */
class BigIntegerTypeConverter : TypeConverter<BigInteger>() {

    /**
     * Converts the specified value to the [BigInteger] type.
     *
     * @param value The value to convert.
     * @return Returns an [BigInteger] representation of the specified value.
     */
    override fun convert(value: Any): BigInteger = when (value) {
        is Boolean -> if (value) BigInteger.ONE else BigInteger.ZERO
        is Byte -> BigInteger.valueOf(value.toLong())
        is Short -> BigInteger.valueOf(value.toLong())
        is Int -> BigInteger.valueOf(value.toLong())
        is Long -> BigInteger.valueOf(value)
        is BigInteger -> value
        is Float -> value.toBigIntegerChecked()
        is Double -> value.toBigIntegerChecked()
        is BigDecimal -> value.toBigIntegerChecked()
        is String -> value.toBigInteger()
        is Char -> BigInteger.valueOf(value.toLong())
        else -> throw IllegalTypeConversionException(value.kotlinClass, BigInteger::class)
    }

    /**
     * Performs a checked conversion from [Float] to [BigInteger].
     *
     * @return Returns a [BigInteger] representation of the specified [Float].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [BigInteger].
     */
    private fun Float.toBigIntegerChecked(): BigInteger = when {
        (!isInteger()) -> throw NUMERIC_LOSS_OF_PRECISION
        else -> BigInteger.valueOf(toLong())
    }

    /**
     * Performs a checked conversion from [Double] to [BigInteger].
     *
     * @return Returns a [BigInteger] representation of the specified [Double].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [BigInteger].
     */
    private fun Double.toBigIntegerChecked(): BigInteger = when {
        (!isInteger()) -> throw NUMERIC_LOSS_OF_PRECISION
        else -> BigInteger.valueOf(toLong())
    }

    /**
     * Performs a checked conversion from [BigDecimal] to [BigInteger].
     *
     * @return Returns a [BigInteger] representation of the specified [BigDecimal].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [BigInteger].
     */
    private fun BigDecimal.toBigIntegerChecked(): BigInteger = when {
        (!isInteger()) -> throw NUMERIC_LOSS_OF_PRECISION
        else -> toBigInteger()
    }
}