package io.onixlabs.kotlin.projection

import io.onixlabs.kotlin.core.reflection.createInstance
import io.onixlabs.kotlin.core.reflection.kotlinClass
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmErasure

/**
 * Represents the base class for implementing type projectors.
 *
 * @param T The underlying type of the object being projected from.
 * @param R The underlying type of the object being projected to.
 */
abstract class Projector<T : Any, R : Any> {

    /**
     * Gets the underlying class type of the generic type parameter [R].
     * This allows the type to identified even for anonymous projector implementations through type reification.
     */
    internal open val genericTargetTypeKotlinClass: KClass<*>
        get() = kotlinClass.supertypes[0].arguments[1].type?.jvmErasure!!

    companion object {

        /**
         * Projects the specified subject to an instance of the specified type [R].
         *
         * @param R The underlying type of the object being projected to.
         * @param subject The object being projected from.
         * @param configuration Specifies the type converter configuration to use for projection.
         * @return Returns a projected instance of the specified type [R].
         */
        inline fun <reified R : Any> project(
            subject: Any,
            configuration: TypeConverterConfiguration = TypeConverterConfiguration.DEFAULT
        ): R {
            return object : Projector<Any, R>() {
                override val genericTargetTypeKotlinClass: KClass<*> get() = R::class
                override fun project(builder: ProjectionBuilder<Any, R>) = Unit
            }.project(subject, configuration)
        }

        /**
         * Projects the specified subject to an instance of the specified type [R].
         *
         * @param T The underlying type of the object being projected from.
         * @param R The underlying type of the object being projected to.
         * @param subject The object being projected from.
         * @param configuration Specifies the type converter configuration to use for projection.
         * @param builderAction Specifies any unconventional projection to be handled by the builder.
         * @return Returns a projected instance of the specified type [R].
         */
        inline fun <T : Any, reified R : Any> project(
            subject: T,
            configuration: TypeConverterConfiguration = TypeConverterConfiguration.DEFAULT,
            crossinline builderAction: ProjectionBuilder<T, R>.() -> Unit
        ): R {
            return object : Projector<T, R>() {
                override val genericTargetTypeKotlinClass: KClass<*> get() = R::class
                override fun project(builder: ProjectionBuilder<T, R>) = builderAction(builder)
            }.project(subject, configuration)
        }
    }

    /**
     * Projects the specified subject to an instance of the specified type [R].
     *
     * @param subject The object being projected from.
     * @param configuration Specifies the type converter configuration to use for projection.
     * @return Returns a projected instance of the specified type [R].
     * @throws ProjectionException if the projection fails.
     */
    @Suppress("UNCHECKED_CAST")
    fun project(subject: T, configuration: TypeConverterConfiguration = TypeConverterConfiguration.DEFAULT): R {
        val builder = ProjectionBuilder(subject, genericTargetTypeKotlinClass as KClass<R>, configuration)
        project(builder)
        return try {
            genericTargetTypeKotlinClass.createInstance(builder.compile()) as R
        } catch (pex: ProjectionException) {
            throw pex
        } catch (ex: Exception) {
            throw ProjectionException("Projection failed. ${ex.message}.", ex)
        }
    }

    /**
     * Defines unconventional projection actions.
     *
     * @param builder The projection builder which will be used to build unconventional projections.
     */
    protected abstract fun project(builder: ProjectionBuilder<T, R>)
}