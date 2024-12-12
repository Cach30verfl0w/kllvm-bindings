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

package de.cacheoverflow.llvm

import kotlinx.io.files.Path
import org.lwjgl.llvm.LLVMCore

internal actual fun loadLLVM0(basePath: Path, libraryFileName: String): Boolean {
    System.setProperty("org.lwjgl.librarypath", Path(basePath, "lib").toString())
    System.setProperty("org.lwjgl.llvm.libname", libraryFileName)
    try {
        LLVMCore.getLibrary()
        return true
    } catch (_: UnsatisfiedLinkError) {
        return false
    }
}

internal actual fun unloadLLVM0() = LLVMCore.getLibrary().close()
