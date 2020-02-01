package io.onixlabs.kotlin.projection.typeconverters

import io.onixlabs.kotlin.core.reflection.kotlinClass
import io.onixlabs.kotlin.projection.IllegalTypeConversionException
import io.onixlabs.kotlin.projection.TypeConverter
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Provides a mechanism to safely convert the specified value value to the [BigDecimal] type.
 */
class BigDecimalTypeConverter : TypeConverter<BigDecimal>() {

    /**
     * Converts the specified value to the [BigDecimal] type.
     *
     * @param value The value to convert.
     * @return Returns a [BigDecimal] representation of the specified value.
     */
    override fun convert(value: Any): BigDecimal = when (value) {
        is Boolean -> if (value) BigDecimal.ONE else BigDecimal.ZERO
        is Byte -> BigDecimal.valueOf(value.toLong())
        is Short -> BigDecimal.valueOf(value.toLong())
        is Int -> BigDecimal.valueOf(value.toLong())
        is Long -> BigDecimal.valueOf(value)
        is BigInteger -> BigDecimal(value)
        is Double -> value.toBigDecimal()
        is BigDecimal -> value
        is String -> value.toBigDecimal().stripTrailingZeros()
        is Char -> BigDecimal.valueOf(value.toLong())
        else -> throw IllegalTypeConversionException(value.kotlinClass, BigDecimal::class)
    }
}