package io.onixlabs.kotlin.projection

import kotlin.reflect.KClass

/**
 * Represents the exception that is thrown when a type converter is unable to convert the specified value.
 *
 * @param message A message detailing the type conversion exception.
 * @param cause An underlying cause of the type conversion exception.
 */
class IllegalTypeConversionException(message: String, cause: Throwable? = null) : Exception(message, cause) {

    internal companion object {

        /**
         * An [IllegalTypeConversionException] that is thrown when a numeric value overflows.
         */
        val NUMERIC_OVERFLOW = IllegalTypeConversionException("Illegal type conversion. Numeric overflow.")

        /**
         * An [IllegalTypeConversionException] that is thrown when a floating point value loses precision.
         */
        val NUMERIC_LOSS_OF_PRECISION = IllegalTypeConversionException("Illegal type conversion. Loss of precision.")
    }

    /**
     * Creates an [IllegalTypeConversionException] from the specified input and output classes.
     * This exception details that the input class cannot be mapped to the output class.
     *
     * @param inputClass The input class.
     * @param outputClass The output class.
     */
    constructor(inputClass: KClass<*>, outputClass: KClass<*>) : this(
        "Illegal type conversion. Cannot convert from '${inputClass.qualifiedName}' to '${outputClass.qualifiedName}'."
    )
}