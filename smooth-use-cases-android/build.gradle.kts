plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-android-extensions")
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
    implementation(project(":smooth-use-cases"))
    implementation(project(":smooth-store"))
    implementation(project(":smooth-store-memory"))
    implementation(WorkManager.RUNTIME)
    implementation(WorkManager.KTX)
    implementation(Kotlin.REFLECT)
    implementation(project(":smooth-constraints-android"))
    implementation(project(":smooth-constraints-work"))
}
