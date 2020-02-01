package io.onixlabs.kotlin.projection

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*

data class Post(
    val title: String,
    val text: String,
    val date: LocalDate
)

data class Source(
    val id: String,
    val firstName: String,
    val lastName: String,
    val description: String?,
    val posts: Iterable<Post>
)

data class Target(
    val id: UUID,
    val fullName: String,
    val description: String,
    val postCount: Int,
    val posts: Iterable<Post>,
    val reference: Long = Long.MAX_VALUE
)

class SourceToTargetProjector : Projector<Source, Target>() {
    override fun project(builder: ProjectionBuilder<Source, Target>) {

        builder.parameter(Target::fullName, "${builder.subject.firstName} ${builder.subject.lastName}")

        builder.parameter(Target::description) {
            it.description ?: "No description"
        }

        builder.parameter(Target::postCount, builder.subject.posts.count())
    }
}

class ComplexProjectorTests {

    @Test
    fun `Projector can project source object to target with no null parameters`() {

        // Arrange
        val source = Source(
            id = "079861fc-97f2-4af5-a47b-ece5a433e623",
            firstName = "John",
            lastName = "Smith",
            description = "Hello, world!",
            posts = listOf(
                Post("My first post", "Lorem ipsum...", LocalDate.of(2020, 1, 1))
            )
        )

        val expected = Target(
            id = UUID.fromString("079861fc-97f2-4af5-a47b-ece5a433e623"),
            fullName = "John Smith",
            description = "Hello, world!",
            postCount = 1,
            posts = listOf(
                Post("My first post", "Lorem ipsum...", LocalDate.of(2020, 1, 1))
            ),
            reference = Long.MAX_VALUE
        )

        // Act
        val target = Projector.project<Source, Target>(source) {
            parameter(Target::fullName, "${subject.firstName} ${subject.lastName}")

            parameter(Target::description) {
                it.description ?: "No description"
            }

            parameter(Target::postCount, subject.posts.count())
        }

        // Assert
        assertEquals(expected, target)
    }

    @Test
    fun `SourceToTargetProjector can project source object to target with no null parameters`() {

        // Arrange
        val source = Source(
            id = "079861fc-97f2-4af5-a47b-ece5a433e623",
            firstName = "John",
            lastName = "Smith",
            description = "Hello, world!",
            posts = listOf(
                Post("My first post", "Lorem ipsum...", LocalDate.of(2020, 1, 1))
            )
        )

        val expected = Target(
            id = UUID.fromString("079861fc-97f2-4af5-a47b-ece5a433e623"),
            fullName = "John Smith",
            description = "Hello, world!",
            postCount = 1,
            posts = listOf(
                Post("My first post", "Lorem ipsum...", LocalDate.of(2020, 1, 1))
            ),
            reference = Long.MAX_VALUE
        )

        val projector = SourceToTargetProjector()

        // Act
        val target = projector.project(source)

        // Assert
        assertEquals(expected, target)
    }
}