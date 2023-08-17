plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    id ("com.google.gms.google-services")
    id ("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
    id ("com.google.firebase.crashlytics")
}

android {
    namespace = "com.peterchege.pchat"
    compileSdk= 34

    defaultConfig {
        applicationId = "com.peterchege.pchat"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled =  false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility =JavaVersion.VERSION_17
        targetCompatibility =JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        compose= true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
kotlin {
    sourceSets.configureEach {
        kotlin.srcDir("$buildDir/generated/ksp/$name/kotlin/")
    }
}


dependencies {

    implementation ("androidx.core:core-ktx:1.10.1")
    implementation ("androidx.compose.ui:ui:1.6.0-alpha03")
    implementation ("androidx.compose.material:material:1.6.0-alpha03")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.6.0-alpha03")
    implementation ("androidx.activity:activity-compose:1.7.2")
    implementation ("androidx.compose.material:material-icons-extended:1.6.0-alpha03")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.6.0-alpha03")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.6.0-alpha03")


    // foundation
    implementation ("androidx.compose.foundation:foundation:1.5.0")
    implementation ("androidx.compose.foundation:foundation-layout:1.5.0")


    //dependencies
    // retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    //kotlin serialization & converter
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // view model
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    //coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // dagger hilt
    implementation ("com.google.dagger:hilt-android:2.45")
    kapt ("com.google.dagger:hilt-android-compiler:2.45")
    kapt ("androidx.hilt:hilt-compiler:1.0.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation ("androidx.navigation:navigation-compose:2.7.0")

    // coil
    implementation ("io.coil-kt:coil-compose:2.4.0")

    //firebase
    implementation ("com.google.firebase:firebase-messaging:23.2.1")
    implementation ("com.google.firebase:firebase-auth-ktx:22.1.1")
    implementation ("com.google.android.gms:play-services-auth:20.6.0")
    implementation ("com.google.firebase:firebase-crashlytics-ktx:18.4.0")
    implementation ("com.google.firebase:firebase-analytics-ktx:21.3.0")

    //data store
    implementation ("androidx.datastore:datastore:1.0.0")
    implementation ("androidx.datastore:datastore-core:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")


    // room
    implementation ("androidx.room:room-runtime:2.5.2")
    kapt ("androidx.room:room-compiler:2.5.2")
    implementation ("androidx.room:room-ktx:2.5.2")


    implementation ("com.github.skydoves:landscapist-glide:2.1.8")
    implementation ("com.google.code.gson:gson:2.10")

    implementation ("io.socket:socket.io-client:2.0.0") {
        exclude(group = "org.json", module = "json")
    }

    debugImplementation ("com.github.chuckerteam.chucker:library:3.5.2")
    releaseImplementation ("com.github.chuckerteam.chucker:library-no-op:3.5.2")

    //timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // workmanager
    implementation ("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.hilt:hilt-work:1.0.0")
    implementation("androidx.hilt:hilt-common:1.0.0")



}