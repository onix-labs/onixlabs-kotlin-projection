package io.onixlabs.kotlin.projection.typeconverters

import io.onixlabs.kotlin.core.ONE
import io.onixlabs.kotlin.core.ZERO
import io.onixlabs.kotlin.core.reflection.kotlinClass
import io.onixlabs.kotlin.core.toBigInteger
import io.onixlabs.kotlin.projection.IllegalTypeConversionException
import io.onixlabs.kotlin.projection.IllegalTypeConversionException.Companion.NUMERIC_OVERFLOW
import io.onixlabs.kotlin.projection.TypeConverter
import java.math.BigInteger

/**
 * Provides a mechanism to safely convert the specified value value to the [Double] type.
 */
class DoubleTypeConverter : TypeConverter<Double>() {
    override fun convert(value: Any): Double = when (value) {
        is Boolean -> if (value) Double.ONE else Double.ZERO
        is Byte -> value.toDouble()
        is Short -> value.toDouble()
        is Int -> value.toDouble()
        is Long -> value.toDouble()
        is BigInteger -> value.toDoubleChecked()
        is Double -> value
        is String -> value.toDouble()
        is Char -> value.toDouble()
        else -> throw IllegalTypeConversionException(value.kotlinClass, Double::class)
    }

    /**
     * Performs a checked conversion from [BigInteger] to [Double].
     *
     * @return Returns a [Double] representation of the specified [BigInteger].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [Double].
     */
    private fun BigInteger.toDoubleChecked(): Double = when {
        (this !in Double.MIN_VALUE.toBigInteger()..Double.MAX_VALUE.toBigInteger()) -> throw NUMERIC_OVERFLOW
        else -> toDouble()
    }
}