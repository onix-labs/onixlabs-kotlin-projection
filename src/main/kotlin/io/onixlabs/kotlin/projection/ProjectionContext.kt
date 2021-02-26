/**
 * Copyright 2020 Matthew Layton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.onixlabs.kotlin.projection

import io.onixlabs.kotlin.core.reflection.formattedQualifiedName
import io.onixlabs.kotlin.core.reflection.kotlinClass
import io.onixlabs.kotlin.core.typeconverters.TypeConverter
import kotlin.reflect.KParameter

/**
 * Represents a projection context.
 *
 * @param parameter The parameter to project.
 * @param value The value to convert.
 * @param converter The type converter that will be used to convert the specified value.
 */
internal class ProjectionContext(val parameter: KParameter, val converter: TypeConverter<*>, val value: Any?) {

    private val parameterName: String = parameter.name
        ?: throw IllegalArgumentException("Cannot project the specified un-named parameter: $parameter")

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
        value != null -> parameterName to getConvertedValue(value)
        parameter.isOptional -> null
        parameter.type.isMarkedNullable -> parameterName to null
        else -> throw ProjectionException("Cannot project '$value' to the specified non-optional and non-nullable parameter: '$parameterName'.")
    }

    /**
     * Converts the value for projection.
     *
     * @param value The value to convert.
     * @return Returns the converted value.
     * @throws ProjectionException if the value could not be converted.
     */
    private fun getConvertedValue(value: Any): Any? = try {
        converter.convert(value)
    } catch (throwable: Throwable) {
        val fromTypeName = value.kotlinClass.qualifiedName
        val toTypeName = parameter.type.formattedQualifiedName
        throw ProjectionException(buildString {
            append("Projection failed for the specified parameter: $parameterName. ")
            append("Could not convert from '$fromTypeName' to '$toTypeName'.")
        }, throwable)
    }
}
