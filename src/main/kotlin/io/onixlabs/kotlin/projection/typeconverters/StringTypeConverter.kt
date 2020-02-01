package io.onixlabs.kotlin.projection.typeconverters

import io.onixlabs.kotlin.projection.TypeConverter

/**
 * Provides a mechanism to safely convert the specified value value to the [String] type.
 */
class StringTypeConverter : TypeConverter<String>() {

    /**
     * Converts the specified value to the [String] type.
     *
     * @param value The value to convert.
     * @return Returns a [String] representation of the specified value.
     */
    override fun convert(value: Any): String = when (value) {
        // TODO : [Kotlin 1.3] Push values to TRUE_STRING and FALSE_STRING in Boolean.Companion
        is Boolean -> if (value) "True" else "False"
        else -> value.toString()
    }
}