package io.onixlabs.kotlin.projection

/**
 * Represents an exception that is thrown when projection fails.
 *
 * @param message A message detailing the projection exception.
 * @param cause An underlying cause of the projection exception.
 */
class ProjectionException(message: String, cause: Throwable? = null) : Exception(message, cause)