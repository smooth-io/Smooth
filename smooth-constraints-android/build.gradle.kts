version = LibraryAndroidCoordinates.LIBRARY_VERSION

plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-android-extensions")
//    id("maven-publish")
    kotlin("kapt")

}

android {
    compileSdkVersion(Sdk.COMPILE_SDK_VERSION)

    defaultConfig {
        minSdkVersion(Sdk.MIN_SDK_VERSION)
        targetSdkVersion(Sdk.TARGET_SDK_VERSION)

        versionCode = LibraryAndroidCoordinates.LIBRARY_VERSION_CODE
        versionName = LibraryAndroidCoordinates.LIBRARY_VERSION

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    lintOptions {
        isWarningsAsErrors = true
        isAbortOnError = true
    }
}

defaultDependencies {
    implementation(SupportLibs.ANDROIDX_APPCOMPAT)
    implementation(Startup.RUNTIME)
    implementation(project(":smooth-constraints"))
    implementation(project(":smooth-network"))
    implementation(project(":smooth-battery"))
    implementation("com.nabinbhandari.android:permissions:3.8")
    coroutine()
}
//
//afterEvaluate {
//    publishing {
//        publications {
//            create<MavenPublication>("release") {
//                from(components["release"])
//            }
//        }
//    }
//}
