buildscript {
    dependencies {
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.46")
//        classpath ("com.google.gms:google-services:4.3.1")
        classpath ("com.google.gms:google-services:4.4.1")

        val nav_version = "2.5.0"
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}


// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
//    kotlin("kapt") version "1.9.0"
}