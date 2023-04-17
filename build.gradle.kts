plugins {
    id("com.diffplug.spotless") version "5.3.0"
}
buildscript {
    repositories {
        google()
        mavenCentral()

    }

    dependencies {
        classpath ("com.android.tools.build:gradle:7.3.1")
        classpath( "com.google.dagger:hilt-android-gradle-plugin:2.45")
        classpath ("com.google.gms:google-services:4.3.15")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        classpath ("org.jetbrains.kotlin:kotlin-serialization:1.8.10")
    }
}



apply(plugin = "com.diffplug.spotless")
spotless {
    kotlin {
        target("**/*.kt")
        licenseHeaderFile(
            rootProject.file("${project.rootDir}/spotless/LICENSE.txt"),
            "^(package|object|import|interface)"
        )
    }
}