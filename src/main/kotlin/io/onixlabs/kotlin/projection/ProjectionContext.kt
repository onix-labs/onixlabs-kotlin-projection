package io.onixlabs.kotlin.projection

/**
 * Represents a projection context.
 *
 * @param parameterName The name of the target parameter for which to provide a value.
 * @param isMarkedNullable Determines whether the target parameter is marked nullable.
 * @param isOptional Determines whether the target parameter is optional.
 * @param value The value to convert.
 * @param typeConverter The type converter that will be used to convert the specified value.
 */
internal data class ProjectionContext(
    val parameterName: String,
    val isMarkedNullable: Boolean,
    val isOptional: Boolean,
    val value: Any?,
    val typeConverter: TypeConverter<*>
) {
    /**
     * Gets a converted map entry pair for the specified parameter.
     *
     * If the value is not null then an entry will be returned for the converted value.
     * If the value is null and the parameter is optional then no entry is returned.
     * If the value is null and the parameter is marked nullable then an entry for a null parameter is returned.
     *
     * @return Returns an entry pair for the parameter, or null if the parameter is optional and the value is null.
     * @throws ProjectionException if the value is null and the parameter is non-nullable and non-optional.
     */
    fun getConvertedMapEntryOrNull(): Pair<String, Any?>? = when {
        value != null -> parameterName to typeConverter.convert(value)
        isOptional -> null
        isMarkedNullable -> parameterName to null
        else -> throw ProjectionException(
            "Projection failed. Cannot map null to non-nullable and non-optional parameter '$parameterName'."
        )
    }
}