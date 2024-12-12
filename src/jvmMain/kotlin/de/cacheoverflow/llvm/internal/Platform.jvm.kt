/**
 * Copyright 2024 Cedric Hammes
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.cacheoverflow.llvm.internal

import kotlin.IllegalStateException

internal actual fun getPlatform(): Platform {
    val osName = System.getProperty("os.name")
    return when {
        osName.startsWith("Linux") || osName.startsWith("SunOS") || osName.startsWith("Unix") -> Platform.LINUX
        osName.startsWith("Mac OS X") || osName.startsWith("Darwin") -> Platform.MACOS
        osName.startsWith("Windows") -> Platform.WINDOWS
        else -> throw IllegalStateException("+")
    }
}
