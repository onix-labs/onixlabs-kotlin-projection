package io.onixlabs.kotlin.projection

import io.onixlabs.kotlin.projection.typeconverters.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

class TypeConverterConfigurationTests {

    @Test
    fun `TypeConverterConfiguration DEFAULT should return a BigDecimalTypeConverter`() {

        // Arrange
        val configuration = TypeConverterConfiguration.DEFAULT

        // Act
        val result: Any = configuration.getTypeConverter<BigDecimal>()

        // Assert
        assert(result is BigDecimalTypeConverter)
    }

    @Test
    fun `TypeConverterConfiguration DEFAULT should return a BigIntegerTypeConverter`() {

        // Arrange
        val configuration = TypeConverterConfiguration.DEFAULT

        // Act
        val result: Any = configuration.getTypeConverter<BigInteger>()

        // Assert
        assert(result is BigIntegerTypeConverter)
    }

    @Test
    fun `TypeConverterConfiguration DEFAULT should return a BooleanTypeConverter`() {

        // Arrange
        val configuration = TypeConverterConfiguration.DEFAULT

        // Act
        val result: Any = configuration.getTypeConverter<Boolean>()

        // Assert
        assert(result is BooleanTypeConverter)
    }

    @Test
    fun `TypeConverterConfiguration DEFAULT should return a ByteTypeConverter`() {

        // Arrange
        val configuration = TypeConverterConfiguration.DEFAULT

        // Act
        val result: Any = configuration.getTypeConverter<Byte>()

        // Assert
        assert(result is ByteTypeConverter)
    }

    @Test
    fun `TypeConverterConfiguration DEFAULT should return a CharTypeConverter`() {

        // Arrange
        val configuration = TypeConverterConfiguration.DEFAULT

        // Act
        val result: Any = configuration.getTypeConverter<Char>()

        // Assert
        assert(result is CharTypeConverter)
    }

    @Test
    fun `TypeConverterConfiguration DEFAULT should return a DoubleTypeConverter`() {

        // Arrange
        val configuration = TypeConverterConfiguration.DEFAULT

        // Act
        val result: Any = configuration.getTypeConverter<Double>()

        // Assert
        assert(result is DoubleTypeConverter)
    }

    @Test
    fun `TypeConverterConfiguration DEFAULT should return a FloatTypeConverter`() {

        // Arrange
        val configuration = TypeConverterConfiguration.DEFAULT

        // Act
        val result: Any = configuration.getTypeConverter<Float>()

        // Assert
        assert(result is FloatTypeConverter)
    }

    @Test
    fun `TypeConverterConfiguration DEFAULT should return a IntTypeConverter`() {

        // Arrange
        val configuration = TypeConverterConfiguration.DEFAULT

        // Act
        val result: Any = configuration.getTypeConverter<Int>()

        // Assert
        assert(result is IntTypeConverter)
    }

    @Test
    fun `TypeConverterConfiguration DEFAULT should return a LongTypeConverter`() {

        // Arrange
        val configuration = TypeConverterConfiguration.DEFAULT

        // Act
        val result: Any = configuration.getTypeConverter<Long>()

        // Assert
        assert(result is LongTypeConverter)
    }

    @Test
    fun `TypeConverterConfiguration DEFAULT should return a ShortTypeConverter`() {

        // Arrange
        val configuration = TypeConverterConfiguration.DEFAULT

        // Act
        val result: Any = configuration.getTypeConverter<Short>()

        // Assert
        assert(result is ShortTypeConverter)
    }

    @Test
    fun `TypeConverterConfiguration DEFAULT should return a StringTypeConverter`() {

        // Arrange
        val configuration = TypeConverterConfiguration.DEFAULT

        // Act
        val result: Any = configuration.getTypeConverter<String>()

        // Assert
        assert(result is StringTypeConverter)
    }

    @Test
    fun `TypeConverterConfiguration DEFAULT should return a UUIDTypeConverter`() {

        // Arrange
        val configuration = TypeConverterConfiguration.DEFAULT

        // Act
        val result: Any = configuration.getTypeConverter<UUID>()

        // Assert
        assert(result is UUIDTypeConverter)
    }

    @Test
    fun `TypeConverterConfiguration DEFAULT should throw an exception when there is no available type converter`() {

        // Arrange
        val configuration = TypeConverterConfiguration.DEFAULT

        // Act
        val result = assertThrows<IllegalArgumentException> {
            configuration.getTypeConverter<List<*>>()
        }

        // Assert
        assertEquals("No type converter registered for 'kotlin.collections.List'.", result.message)
    }
}