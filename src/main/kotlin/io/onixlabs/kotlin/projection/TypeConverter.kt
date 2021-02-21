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

/**
 * Represents the base class for implementing type converters.
 *
 * @param T The underlying type that the type converter will convert to.
 */
abstract class TypeConverter<T> {

    /**
     * Converts the specified value to the [T] type.
     *
     * @param value The value to convert.
     * @return Returns a [T] representation of the specified value.
     */
    abstract fun convert(value: Any): T
}
