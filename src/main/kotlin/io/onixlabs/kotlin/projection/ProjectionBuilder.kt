package io.onixlabs.kotlin.projection

import io.onixlabs.kotlin.core.reflection.getPrimaryConstructor
import io.onixlabs.kotlin.core.reflection.getPropertyOrNull
import io.onixlabs.kotlin.core.reflection.kotlinClass
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.jvmErasure

/**
 * Represents a builder to construct unconventional projection rules.
 *
 * @param T The underlying type of the object being projected from.
 * @param R The underlying type of the object being projected to.
 * @param subject The object being projected from.
 * @param targetClass The class of the object being projected to.
 * @param configuration Specifies the type converters to use in a projection.
 */
class ProjectionBuilder<T : Any, R : Any>(
    val subject: T,
    private val targetClass: KClass<R>,
    private val configuration: TypeConverterConfiguration
) {

    /**
     * A mutable map of each target parameter and its projection context.
     * The map is mutable to allow unconventional projection contexts to replace conventional ones.
     */
    private val contextMap: MutableMap<String, ProjectionContext> = initializeContextMap()

    /**
     * Creates an unconventional projection context for the specified property and projection value.
     *
     * @param P The underlying type of the parameter.
     * @param property The target property for which to create a projection context.
     * @param projectionValue The value to be projected to the target object.
     * @throws IllegalArgumentException if the property is not also a parameter of the target object's constructor.
     */
    fun <P> parameter(property: KProperty1<R, P>, projectionValue: P) {
        setTypeConverter(property.name, object : TypeConverter<P>() {
            override fun convert(value: Any): P = projectionValue
        }, Projected)
    }

    /**
     * Creates an unconventional projection context for the specified property and projection action.
     *
     * @param P The underlying type of the parameter.
     * @param property The target property for which to create a projection context.
     * @param projectionAction An action which returns the value to be projected to the target object.
     * @throws IllegalArgumentException if the property is not also a parameter of the target object's constructor.
     */
    fun <P> parameter(property: KProperty1<R, P>, projectionAction: (T) -> P) {
        setTypeConverter(property.name, object : TypeConverter<P>() {
            override fun convert(value: Any): P = projectionAction(subject)
        }, Projected)
    }

    /**
     * Creates an unconventional projection context for the specified property and type converter.
     *
     * @param P The underlying type of the parameter.
     * @param property The target property for which to create a projection context.
     * @param typeConverter The type converter that will be used to convert the value to the target object.
     * @throws IllegalArgumentException if the property is not also a parameter of the target object's constructor.
     */
    fun <P> parameter(property: KProperty1<R, P>, typeConverter: TypeConverter<P>) {
        setTypeConverter(property.name, typeConverter)
    }

    /**
     * Creates an unconventional projection context for the specified properties.
     * This is useful when mapping properties of identical type, but with different names.
     *
     * @param P The underlying type of the parameter.
     * @param targetProperty The target property for which to create a projection context.
     * @param sourceProperty The source property which returns the value to be projected to the target object.
     * @throws IllegalArgumentException if the property is not also a parameter of the target object's constructor.
     */
    fun <P> parameter(targetProperty: KProperty1<R, P>, sourceProperty: KProperty1<T, P>) {
        setTypeConverter(targetProperty.name, object : TypeConverter<P>() {
            override fun convert(value: Any): P = sourceProperty.get(subject)
        }, Projected)
    }

    /**
     * Creates an unconventional projection context for the specified parameter and projection value.
     *
     * @param P The underlying type of the parameter.
     * @param parameter The target parameter for which to create a projection context.
     * @param projectionValue The value to be projected to the target object.
     * @throws IllegalArgumentException if the parameter is not a parameter of the target object's constructor.
     */
    fun <P> parameter(parameter: String, projectionValue: P) {
        setTypeConverter(parameter, object : TypeConverter<P>() {
            override fun convert(value: Any): P = projectionValue
        }, Projected)
    }

    /**
     * Creates an unconventional projection context for the specified parameter and projection action.
     *
     * @param P The underlying type of the parameter.
     * @param parameter The target parameter for which to create a projection context.
     * @param projectionAction An action which returns the value to be projected to the target object.
     * @throws IllegalArgumentException if the parameter is not a parameter of the target object's constructor.
     */
    fun <P> parameter(parameter: String, projectionAction: (T) -> P) {
        setTypeConverter(parameter, object : TypeConverter<P>() {
            override fun convert(value: Any): P = projectionAction(subject)
        }, Projected)
    }

    /**
     * Creates an unconventional projection context for the specified parameter and type converter.
     *
     * @param P The underlying type of the parameter.
     * @param parameter The target parameter for which to create a projection context.
     * @param typeConverter The type converter that will be used to convert the value to the target object.
     * @throws IllegalArgumentException if the parameter is not a parameter of the target object's constructor.
     */
    fun <P> parameter(parameter: String, typeConverter: TypeConverter<P>) {
        setTypeConverter(parameter, typeConverter)
    }

    /**
     * Creates an unconventional projection context for the specified properties.
     * This is useful when mapping properties of identical type, but with different names.
     *
     * @param P The underlying type of the parameter.
     * @param parameter The target parameter for which to create a projection context.
     * @param property The source property which returns the value to be projected to the target object.
     * @throws IllegalArgumentException if the parameter is not a parameter of the target object's constructor.
     */
    fun <P> parameter(parameter: String, property: KProperty1<T, P>) {
        setTypeConverter(parameter, object : TypeConverter<P>() {
            override fun convert(value: Any): P = property.get(subject)
        }, Projected)
    }

    /**
     * Compiles the map of projection contexts to a map of named arguments.
     *
     * @return Returns a map of named arguments from which the target object can be constructed.
     */
    internal fun compile(): Map<String, Any?> {
        return contextMap.mapNotNull { it.value.getConvertedMapEntryOrNull() }.toMap()
    }

    /**
     * Initializes the projection context map with conventional projections.
     *
     * @return Returns a mutable map of conventional projections.
     * @throws IllegalStateException if any of the target object's constructor parameters are unnamed.
     */
    private fun initializeContextMap(): MutableMap<String, ProjectionContext> {
        return targetClass.getPrimaryConstructor().parameters.map {

            val name = it.name ?: throw IllegalStateException("Illegal state. Cannot map unnamed parameters.")
            val property = subject.kotlinClass.getPropertyOrNull(name)
            val value = property?.get(subject)
            val typeConverter = getTypeConverter(it.type.jvmErasure, property?.returnType?.jvmErasure)

            name to ProjectionContext(
                parameterName = name,
                isMarkedNullable = it.type.isMarkedNullable,
                isOptional = it.isOptional,
                value = value,
                typeConverter = typeConverter
            )

        }.toMap().toMutableMap()
    }

    /**
     * Sets the type converter for an unconventional projection.
     *
     * @param name The name of the parameter whose projection type converter will be updated.
     * @param typeConverter The updated type converter for the projection.
     * @param value Provides a mechanism to inject non-null projection marker objects.
     *
     * A valid value which requires type conversion may already exist on the projection context,
     * in which case it should not be updated.
     * The [Ignored] marker object prevents the value from being updated.
     * The [Projected] marker object is used when custom type converters have been implemented.
     * Since null cannot be converted, [Projected] is injected to provide an insignificant non-null value.
     * With respect to the above, null cannot be passed here because it's a valid candidate for projection.
     *
     * @throws IllegalArgumentException if the parameter is not a parameter of the target object's constructor.
     */
    private fun setTypeConverter(name: String, typeConverter: TypeConverter<*>, value: Any? = Ignored) {
        if (!contextMap.containsKey(name)) {
            throw IllegalArgumentException("Target type does not contain a constructor parameter named '$name'")
        }

        if (value == Ignored) {
            contextMap.replace(name, contextMap[name]!!.copy(typeConverter = typeConverter))
        } else {
            contextMap.replace(name, contextMap[name]!!.copy(typeConverter = typeConverter, value = value))
        }
    }

    /**
     * Gets a conventional type converter for a projection context.
     *
     * If the target class and source class are identical, this returns an [IdenticalTypeConverter] which
     * just returns the specified value as it's result. This allows type conversion for types that are not
     * registered by the specified type converter configuration.
     *
     * If the target class is registered by the specified type converter configuration, it will be returned,
     * otherwise it will return a [NotImplementedTypeConverter] which will throw an exception on compilation.
     *
     * @param targetClass The target class of the object being converted to.
     * @param sourceClass The source class of the object being converted from.
     * @return Returns a conventional [TypeConverter] implementation.
     */
    private fun getTypeConverter(targetClass: KClass<*>, sourceClass: KClass<*>?): TypeConverter<*> {
        return if (sourceClass == targetClass) {
            IdenticalTypeConverter()
        } else {
            configuration.getTypeConverterOrNull(targetClass) ?: NotImplementedTypeConverter()
        }
    }

    /**
     * Represents a type converter to convert identical types that are not known by a type converter configuration.
     */
    private class IdenticalTypeConverter : TypeConverter<Any>() {
        override fun convert(value: Any): Any = value
    }

    /**
     * Represents a type converter which is not implemented and throws an exception.
     */
    private class NotImplementedTypeConverter : TypeConverter<Nothing>() {
        override fun convert(value: Any): Nothing {
            throw IllegalTypeConversionException("Illegal type conversion. The type converter is not implemented.")
        }
    }

    /**
     * Represents a marker object to indicate that a projection context value should not be updated.
     */
    private object Ignored

    /**
     * Represents a marker object to indicate that a projection context is using an unconventional type converter.
     */
    private object Projected
}