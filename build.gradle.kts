plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

group = findProperty("project_group").toString()
version = libs.versions.kotlin.get()

kotlin {
    linuxX64 {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    linuxArm64()
    macosX64()
    macosArm64()
    jvm()
    
    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }
        
        commonMain.dependencies {
            api(libs.kotlinx.io.core)
        }
        jvmMain.dependencies {
            fun addLWJGLDependency(dependency: Provider<MinimalExternalModuleDependency>) {
                implementation(dependency)
                
                // Linux natives
                runtimeOnly(dependencies.variantOf(dependency) { classifier("natives-linux-arm64") })
                runtimeOnly(dependencies.variantOf(dependency) { classifier("natives-linux-riscv64") })
                runtimeOnly(dependencies.variantOf(dependency) { classifier("natives-linux") })
                
                // macOS natives
                runtimeOnly(dependencies.variantOf(dependency) { classifier("natives-macos-arm64") })
                runtimeOnly(dependencies.variantOf(dependency) { classifier("natives-macos") })
                
                // Windows natives
                runtimeOnly(dependencies.variantOf(dependency) { classifier("natives-windows-arm64") })
                runtimeOnly(dependencies.variantOf(dependency) { classifier("natives-windows") })
            }
            
            addLWJGLDependency(libs.lwjgl.base)
            addLWJGLDependency(libs.lwjgl.llvm)
            addLWJGLDependency(libs.lwjgl.rpmalloc)
        }
        nativeMain.dependencies {
            implementation(libs.dlfcn)
        }
    }
}
