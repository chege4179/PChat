buildscript {
    repositories {
        google()
        mavenCentral()

    }

    dependencies {
        classpath ("com.android.tools.build:gradle:7.3.1")
        classpath( "com.google.dagger:hilt-android-gradle-plugin:2.44.2")
        classpath ("com.google.gms:google-services:4.3.14")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        classpath ("org.jetbrains.kotlin:kotlin-serialization:1.7.10")
    }
}



tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}