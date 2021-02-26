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

import io.onixlabs.kotlin.core.reflection.createInstance
import io.onixlabs.kotlin.core.typeconverters.*
import sun.security.x509.ReasonFlags.UNUSED
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.reflect.KClass

/**
 * Represents a type converter configuration.
 */
class TypeConverterConfiguration {

    /**
     * Lists all of the type converters registered in this configuration.
     */
    private val converters: MutableMap<KClass<*>, KClass<*>> = mutableMapOf()

    companion object {

        /**
         * Gets an empty type converter configuration.
         */
        @Suppress(UNUSED)
        val EMPTY: TypeConverterConfiguration
            get() = TypeConverterConfiguration()

        /**
         * Gets the default type converter configuration.
         * This contains all of the type converters implemented in the koto framework.
         */
        val DEFAULT: TypeConverterConfiguration
            get() = TypeConverterConfiguration()
                .setTypeConverter<BigDecimal, BigDecimalTypeConverter>()
                .setTypeConverter<BigInteger, BigIntegerTypeConverter>()
                .setTypeConverter<Boolean, BooleanTypeConverter>()
                .setTypeConverter<Byte, ByteTypeConverter>()
                .setTypeConverter<Char, CharTypeConverter>()
                .setTypeConverter<Double, DoubleTypeConverter>()
                .setTypeConverter<Float, FloatTypeConverter>()
                .setTypeConverter<Int, IntTypeConverter>()
                .setTypeConverter<Long, LongTypeConverter>()
                .setTypeConverter<Short, ShortTypeConverter>()
                .setTypeConverter<String, StringTypeConverter>()
                .setTypeConverter<UUID, UUIDTypeConverter>()
    }

    /**
     * Sets a type converter in the configuration.
     *
     * @param kotlinClass The class of the type to be converted to.
     * @param typeConverter The class of the type converter.
     * @return Returns this [TypeConverterConfiguration] instance.
     */
    fun <T : Any, C : TypeConverter<T>> setTypeConverter(
        kotlinClass: KClass<T>,
        typeConverter: KClass<C>
    ): TypeConverterConfiguration {
        converters[kotlinClass] = typeConverter
        return this
    }

    /**
     * Sets a type converter in the configuration.
     *
     * @param T The class of the type to be converted to.
     * @param C The class of the type converter.
     * @return Returns this [TypeConverterConfiguration] instance.
     */
    inline fun <reified T : Any, reified C : TypeConverter<T>> setTypeConverter(): TypeConverterConfiguration {
        setTypeConverter(T::class, C::class)
        return this
    }

    /**
     * Gets a type converter from this configuration.
     *
     * @param kotlinClass The class of the type to be converted to.
     * @return Returns a type converter for the specified class.
     * @throws IllegalArgumentException if there is no type converter registered for the specified class.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getTypeConverter(kotlinClass: KClass<T>): TypeConverter<T> {
        return getTypeConverterOrNull(kotlinClass)
            ?: throw IllegalArgumentException("No type converter registered for '${kotlinClass.qualifiedName}'.")
    }

    /**
     * Gets a type converter from this configuration, or null if no type converter exists.
     *
     * @param kotlinClass The class of the type to be converted to.
     * @return Returns a type converter for the specified class, or null if no type converter exists.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getTypeConverterOrNull(kotlinClass: KClass<T>?): TypeConverter<T>? {
        return if (kotlinClass != null) {
            converters[kotlinClass]?.createInstance() as? TypeConverter<T>
        } else null
    }

    /**
     * Gets a type converter from this configuration.
     *
     * @param T The class of the type to be converted to.
     * @return Returns a type converter for the specified class.
     * @throws IllegalArgumentException if there is no type converter registered for the specified class.
     */
    inline fun <reified T : Any> getTypeConverter(): TypeConverter<T> = getTypeConverter(T::class)

    /**
     * Gets a type converter from this configuration, or null if no type converter exists.
     *
     * @param T The class of the type to be converted to.
     * @return Returns a type converter for the specified class, or null if no type converter exists.
     */
    @Suppress(UNUSED)
    inline fun <reified T : Any> getTypeConverterOrNull(): TypeConverter<T>? = getTypeConverterOrNull(T::class)
}
