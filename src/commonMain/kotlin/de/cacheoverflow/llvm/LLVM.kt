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

import de.cacheoverflow.llvm.internal.Platform
import de.cacheoverflow.llvm.internal.getPlatform
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.files.SystemPathSeparator

internal expect fun loadLLVM0(basePath: Path, libraryFileName: String): Boolean
internal expect fun unloadLLVM0()

/**
 * @author Cedric Hammes
 * @since  12/12/2024
 */
object LLVM : AutoCloseable {
    private val PLATFORM: Platform = getPlatform()
    private val BASE_PATHS: Array<String> = arrayOf("/usr/share/lib", "/usr/local/opt", "/usr/lib", "/")
    private val SUB_PATHS: Array<String> = arrayOf("llvm-{}/build/Release", "llvm/build/Release", "llvm@{}", "llvm-{}", "llvm")
    private val VERSIONS: ByteArray = byteArrayOf(20, 19, 18, 17, 16)
    private var isLoaded: Boolean = true
    
    val functions: LLVMFunctions
    
    val basePath: Path = run {
        if (PLATFORM == Platform.WINDOWS) Path("C:\\Program Files\\LLVM\\bin") else {
            val separator = SystemPathSeparator.toString()
            for (currentPathBase in BASE_PATHS) {
                val basePath = Path(currentPathBase.replace("/", separator))
                for (currentSubPath in SUB_PATHS) {
                    val subDirectory = currentSubPath.replace("/", separator)
                    for (version in VERSIONS) {
                        val path = Path(basePath, if (subDirectory.contains("{}")) subDirectory.replace("{}", "$version") else subDirectory)
                        if (!SystemFileSystem.exists(path) || !requireNotNull(SystemFileSystem.metadataOrNull(path)).isDirectory)
                            continue
                        
                        return@run path
                    }
                }
            }
            throw NullPointerException("No base path for LLVM installation found")
        }
    }
    
    internal val libraryFileName: String = basePath.let { basePath ->
        val libraryFolder = Path(basePath, "lib")
        if (SystemFileSystem.exists(Path(libraryFolder, "libLLVM.so")))
            return@let "LLVM"
        for (version in VERSIONS) {
            if (!SystemFileSystem.exists(Path(libraryFolder, "libLLVM-$version.so")))
                continue
            
            return@let "LLVM-$version"
        }
        throw NullPointerException("No library file for LLVM installation found")
    }
    
    init {
        if (!loadLLVM0(basePath, libraryFileName)) {
            throw RuntimeException("Unable to load LLVM")
        }
        functions = LLVMFunctions()
    }
    
    override fun close() {
        if (!isLoaded)
            throw IllegalStateException("LLVM was already unloaded")
        unloadLLVM0()
        isLoaded = false
    }
}
