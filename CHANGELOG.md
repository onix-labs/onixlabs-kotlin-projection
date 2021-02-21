![ONIX Labs](https://raw.githubusercontent.com/onix-labs/onix-labs.github.io/master/content/logo/master_full_md.png)

# Change Log

This document serves as the change log for the ONIXLabs Kotlin Projection API.

## Version 1.0.0

#### IllegalTypeConversionException (class)

Represents the exception that is thrown when a type converter is unable to convert the specified value.

#### ProjectionBuilder (class)

Represents a builder to construct unconventional projection rules.

#### ProjectionContext (class)

Represents a projection context.

#### ProjectionException (class)

Represents an exception that is thrown when projection fails.

#### Projector (abstract class)

Represents the base class for implementing type projectors.

#### TypeConverter (abstract class)

Represents the base class for implementing type converters.

#### TypeConverterConfiguration (class)

Represents a type converter configuration.

### Type Converters

The ONIXLabs Kotlin Projection API provides a set of type converters whose responsibility is to safely and conventionally convert a value to the specified type, or fail if the value cannot be converted.

The following type converters have been implemented:

-   BigDecimalTypeConverter
-   BigIntegerTypeConverter
-   BooleanTypeConverter
-   ByteTypeConverter
-   CharTypeConverter
-   DoubleTypeConverter
-   FloatTypeConverter
-   IntTypeConverter
-   LongTypeConverter
-   ShortTypeConverter
-   StringTypeConverter
-   UUIDTypeConverter

