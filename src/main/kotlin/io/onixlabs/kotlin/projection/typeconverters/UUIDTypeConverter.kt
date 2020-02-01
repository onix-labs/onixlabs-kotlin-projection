package io.onixlabs.kotlin.projection.typeconverters

import io.onixlabs.kotlin.core.reflection.kotlinClass
import io.onixlabs.kotlin.projection.IllegalTypeConversionException
import io.onixlabs.kotlin.projection.TypeConverter
import java.util.*

/**
 * Provides a mechanism to safely convert the specified value value to the [UUID] type.
 */
class UUIDTypeConverter : TypeConverter<UUID>() {

    /**
     * Converts the specified value to the [UUID] type.
     *
     * @param value The value to convert.
     * @return Returns a [UUID] representation of the specified value.
     */
    override fun convert(value: Any): UUID = when (value) {
        is String -> value.toUUIDChecked()
        is UUID -> value
        else -> throw IllegalTypeConversionException(value.kotlinClass, UUID::class)
    }

    /**
     * Performs a checked conversion from [String] to [UUID].
     *
     * @return Returns a [UUID] representation of the specified [String].
     * @throws IllegalTypeConversionException if the value cannot be safely converted to [UUID].
     */
    private fun String.toUUIDChecked(): UUID = try {
        UUID.fromString(this)
    } catch (ex: IllegalArgumentException) {
        throw IllegalTypeConversionException("Illegal type conversion. ${ex.message}.")
    }
}