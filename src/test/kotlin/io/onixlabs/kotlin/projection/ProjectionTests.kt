package io.onixlabs.kotlin.projection

import io.onixlabs.kotlin.core.EMPTY
import io.onixlabs.kotlin.core.reflection.kotlinClass
import io.onixlabs.kotlin.core.typeconverters.IllegalTypeConversionException
import io.onixlabs.kotlin.core.typeconverters.TypeConverter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigInteger
import java.util.*

data class NullableSource(val a: Int?)
data class NonNullableSource(val a: Int)
data class NullableTarget(val a: Int?)
data class NonNullableTarget(val a: Int)
data class NonNullableTargetWithUnmatchedPropertyName(val b: Int)
data class NullableOptionalTarget(val a: Int? = 123)
data class NonNullableOptionalTarget(val a: Int = 123)
data class CompatibleNullableTarget(val a: Long?)
data class CompatibleNonNullableTarget(val a: Long)
data class CompatibleNullableOptionalTarget(val a: Long? = 123)
data class CompatibleNonNullableOptionalTarget(val a: Long = 123)
data class IncompatibleMultiArgSource(val value: Int, val power: Int)
data class IncompatibleSingleArgSource(val value: UUID)
data class IncompatibleSingleArgTarget(val value: BigInteger)
data class ParentSource(val a: Child)
data class ParentTarget(val a: Child)
data class Child(val a: Int, val b: String)

class ProjectionTests {


    @Test
    fun `Projector should project identical non-nullable parameters`() {

        // Arrange
        val source = NonNullableSource(123)

        // Act
        val result = Projector.project<NonNullableTarget>(source)

        // Assert
        assertEquals(NonNullableTarget(123), result)
    }

    @Test
    fun `Projector should project identical nullable parameters - with a value`() {

        // Arrange
        val source = NullableSource(123)

        // Act
        val result = Projector.project<NullableTarget>(source)

        // Assert
        assertEquals(NullableTarget(123), result)
    }

    @Test
    fun `Projector should project identical nullable parameters - without a value`() {

        // Arrange
        val source = NullableSource(null)

        // Act
        val result = Projector.project<NullableTarget>(source)

        // Assert
        assertEquals(NullableTarget(null), result)
    }

    @Test
    fun `Projector should project non-nullable parameter to nullable parameter`() {

        // Arrange
        val source = NonNullableSource(123)

        // Act
        val result = Projector.project<NullableTarget>(source)

        // Assert
        assertEquals(NullableTarget(123), result)
    }

    @Test
    fun `Projector should project nullable parameter to non-nullable optional parameter - with a value`() {

        // Arrange
        val source = NullableSource(456)

        // Act
        val result = Projector.project<NonNullableOptionalTarget>(source)

        // Assert
        assertEquals(NonNullableOptionalTarget(456), result)
    }

    @Test
    fun `Projector should project nullable parameter to non-nullable optional parameter - without a value`() {

        // Arrange
        val source = NullableSource(null)

        // Act
        val result = Projector.project<NonNullableOptionalTarget>(source)

        // Assert
        assertEquals(NonNullableOptionalTarget(123), result)
    }

    @Test
    fun `Projector should project nullable parameter to nullable optional parameter - with a value`() {

        // Arrange
        val source = NullableSource(456)

        // Act
        val result = Projector.project<NullableOptionalTarget>(source)

        // Assert
        assertEquals(NullableOptionalTarget(456), result)
    }

    @Test
    fun `Projector should project nullable parameter to nullable optional parameter - without a value`() {

        // Arrange
        val source = NullableSource(null)

        // Act
        val result = Projector.project<NullableOptionalTarget>(source)

        // Assert
        assertEquals(NullableOptionalTarget(123), result)
    }

    @Test
    fun `Projector should throw exception when projecting nullable parameter to non-nullable parameter`() {

        // Arrange
        val source = NullableSource(null)

        // Act
        val result = assertThrows<ProjectionException> { Projector.project<NonNullableTarget>(source) }

        // Assert
        assertEquals(
            "Cannot project 'null' to the specified non-optional and non-nullable parameter: 'a'.",
            result.message
        )
    }

    @Test
    fun `Projector should project compatible non-nullable parameters`() {

        // Arrange
        val source = NonNullableSource(123)

        // Act
        val result = Projector.project<CompatibleNonNullableTarget>(source)

        // Assert
        assertEquals(CompatibleNonNullableTarget(123), result)
    }

    @Test
    fun `Projector should project compatible nullable parameters - with a value`() {

        // Arrange
        val source = NullableSource(123)

        // Act
        val result = Projector.project<CompatibleNullableTarget>(source)

        // Assert
        assertEquals(CompatibleNullableTarget(123), result)
    }

    @Test
    fun `Projector should project compatible nullable parameters - without a value`() {

        // Arrange
        val source = NullableSource(null)

        // Act
        val result = Projector.project<CompatibleNullableTarget>(source)

        // Assert
        assertEquals(CompatibleNullableTarget(null), result)
    }

    @Test
    fun `Projector should project compatible non-nullable parameter to nullable parameter`() {

        // Arrange
        val source = NonNullableSource(123)

        // Act
        val result = Projector.project<CompatibleNullableTarget>(source)

        // Assert
        assertEquals(CompatibleNullableTarget(123), result)
    }

    @Test
    fun `Projector should project compatible nullable parameter to non-nullable optional parameter - with a value`() {

        // Arrange
        val source = NullableSource(456)

        // Act
        val result = Projector.project<CompatibleNonNullableOptionalTarget>(source)

        // Assert
        assertEquals(CompatibleNonNullableOptionalTarget(456), result)
    }

    @Test
    fun `Projector should project compatible nullable parameter to non-nullable optional parameter - without a value`() {

        // Arrange
        val source = NullableSource(null)

        // Act
        val result = Projector.project<CompatibleNonNullableOptionalTarget>(source)

        // Assert
        assertEquals(CompatibleNonNullableOptionalTarget(123), result)
    }

    @Test
    fun `Projector should project compatible nullable parameter to nullable optional parameter - with a value`() {

        // Arrange
        val source = NullableSource(456)

        // Act
        val result = Projector.project<CompatibleNullableOptionalTarget>(source)

        // Assert
        assertEquals(CompatibleNullableOptionalTarget(456), result)
    }

    @Test
    fun `Projector should project compatible nullable parameter to nullable optional parameter - without a value`() {

        // Arrange
        val source = NullableSource(null)

        // Act
        val result = Projector.project<CompatibleNullableOptionalTarget>(source)

        // Assert
        assertEquals(CompatibleNullableOptionalTarget(123), result)
    }

    @Test
    fun `Projector should throw exception when projecting compatible nullable parameter to non-nullable parameter`() {

        // Arrange
        val source = NullableSource(null)

        // Act
        val result = assertThrows<ProjectionException> { Projector.project<CompatibleNonNullableTarget>(source) }

        // Assert
        assertEquals(
            "Cannot project 'null' to the specified non-optional and non-nullable parameter: 'a'.",
            result.message
        )
    }

    @Test
    fun `Projector should project incompatible parameters with custom projection value`() {

        // Arrange
        val source = IncompatibleMultiArgSource(10, 10)

        // Act
        val result = Projector.project<IncompatibleMultiArgSource, IncompatibleSingleArgTarget>(source) {
            parameter(
                IncompatibleSingleArgTarget::value,
                BigInteger.valueOf(subject.value.toLong()).pow(subject.power)
            )
        }

        // Assert
        assertEquals(IncompatibleSingleArgTarget(BigInteger.valueOf(10000000000)), result)
    }

    @Test
    fun `Projector should project incompatible parameters with custom projection action`() {

        // Arrange
        val source = IncompatibleMultiArgSource(10, 10)

        // Act
        val result = Projector.project<IncompatibleMultiArgSource, IncompatibleSingleArgTarget>(source) {
            parameter(IncompatibleSingleArgTarget::value) {
                BigInteger.valueOf(it.value.toLong()).pow(it.power)
            }
        }

        // Assert
        assertEquals(IncompatibleSingleArgTarget(BigInteger.valueOf(10000000000)), result)
    }

    @Test
    fun `Projector should project incompatible parameters with custom type converter`() {

        // Arrange
        val typeConverter = object : TypeConverter<BigInteger>() {
            override fun convert(value: Any): BigInteger = when (value) {
                is UUID -> BigInteger(value.toString().replace("-", String.EMPTY), 16)
                else -> throw IllegalTypeConversionException(value.kotlinClass, BigInteger::class.kotlinClass)
            }
        }

        val source = IncompatibleSingleArgSource(UUID.fromString("00000000-0000-0000-0000-00000000000A"))

        // Act
        val result = Projector.project<IncompatibleSingleArgSource, IncompatibleSingleArgTarget>(source) {
            parameter(IncompatibleSingleArgTarget::value, typeConverter)
        }

        // Assert
        assertEquals(IncompatibleSingleArgTarget(BigInteger.TEN), result)
    }

    @Test
    fun `Projector should project incompatible parameters with custom projection value using string name`() {

        // Arrange
        val source = IncompatibleMultiArgSource(10, 10)

        // Act
        val result = Projector.project<IncompatibleMultiArgSource, IncompatibleSingleArgTarget>(source) {
            parameter("value", BigInteger.valueOf(subject.value.toLong()).pow(subject.power))
        }

        // Assert
        assertEquals(IncompatibleSingleArgTarget(BigInteger.valueOf(10000000000)), result)
    }

    @Test
    fun `Projector should project incompatible parameters with custom projection action using string name`() {

        // Arrange
        val source = IncompatibleMultiArgSource(10, 10)

        // Act
        val result = Projector.project<IncompatibleMultiArgSource, IncompatibleSingleArgTarget>(source) {
            parameter("value") {
                BigInteger.valueOf(it.value.toLong()).pow(it.power)
            }
        }

        // Assert
        assertEquals(IncompatibleSingleArgTarget(BigInteger.valueOf(10000000000)), result)
    }

    @Test
    fun `Projector should project incompatible parameters with custom type converter using string name`() {

        // Arrange
        val typeConverter = object : TypeConverter<BigInteger>() {
            override fun convert(value: Any): BigInteger = when (value) {
                is UUID -> BigInteger(value.toString().replace("-", String.EMPTY), 16)
                else -> throw IllegalTypeConversionException(value.kotlinClass, BigInteger::class.kotlinClass)
            }
        }

        val source = IncompatibleSingleArgSource(UUID.fromString("00000000-0000-0000-0000-00000000000A"))

        // Act
        val result = Projector.project<IncompatibleSingleArgSource, IncompatibleSingleArgTarget>(source) {
            parameter("value", typeConverter)
        }

        // Assert
        assertEquals(IncompatibleSingleArgTarget(BigInteger.TEN), result)
    }

    @Test
    fun `Projector should project parameters with different names`() {

        // Arrange
        val source = NonNullableSource(456)

        // Act
        val result = Projector.project<NonNullableSource, NonNullableTargetWithUnmatchedPropertyName>(source) {
            parameter(NonNullableTargetWithUnmatchedPropertyName::b, NonNullableSource::a)
        }

        // Assert
        assertEquals(NonNullableTargetWithUnmatchedPropertyName(456), result)
    }

    @Test
    fun `Projector should project parameters with different names using string name`() {

        // Arrange
        val source = NonNullableSource(456)

        // Act
        val result = Projector.project<NonNullableSource, NonNullableTargetWithUnmatchedPropertyName>(source) {
            parameter("b", NonNullableSource::a)
        }

        // Assert
        assertEquals(NonNullableTargetWithUnmatchedPropertyName(456), result)
    }

    @Test
    fun `Projector should project identical parameters when there is no registered type converter`() {

        // Arrange
        val source = ParentSource(Child(123, "Hello, world!"))

        // Act
        val result = Projector.project<ParentTarget>(source)

        // Assert
        assertEquals(ParentTarget(Child(123, "Hello, world!")), result)
    }
}