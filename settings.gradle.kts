pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://git.karmakrafts.dev/api/v4/projects/310/packages/maven")
    }
}

rootProject.name = "kllvm-bindings"

