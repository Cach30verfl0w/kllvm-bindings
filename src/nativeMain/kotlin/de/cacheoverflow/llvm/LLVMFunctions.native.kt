/*
 * Copyright $year Cedric Hammes
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

import kotlinx.cinterop.CFunction
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.invoke
import kotlinx.cinterop.usePinned

private typealias LLVMContextCreate = () -> Long
private typealias LLVMContextDispose = (Long) -> Unit
private typealias LLVMModuleCreateWithNameInContext = (COpaquePointer, Long) -> Long
private typealias LLVMDisposeModule = (Long) -> Unit

/**
 * @author Cedric Hammes
 * @since  12/12/2024
 */
actual class LLVMFunctions {
    private val funcLLVMContextCreate: CPointer<CFunction<LLVMContextCreate>> = requireNotNull(library).findFunction("LLVMContextCreate")
    private val funcLLVMContextDispose: CPointer<CFunction<LLVMContextDispose>> = requireNotNull(library).findFunction("LLVMContextDispose")
    private val funcLLVMModuleCreateWithNameInContext: CPointer<CFunction<LLVMModuleCreateWithNameInContext>> =
        requireNotNull(library).findFunction("LLVMModuleCreateWithNameInContext")
    private val funcLLVMDisposeModule: CPointer<CFunction<LLVMDisposeModule>> = requireNotNull(library).findFunction("LLVMDisposeModule")
    
    actual fun createContext(): Long = funcLLVMContextCreate.invoke()
    actual fun disposeContext(handle: Long) = funcLLVMContextDispose(handle)
    actual fun createModule(context: Long, name: String): Long = name.usePinned {
        funcLLVMModuleCreateWithNameInContext(it.addressOf(0), context)
    }
    actual fun disposeModule(handle: Long) = funcLLVMDisposeModule(handle)
}
