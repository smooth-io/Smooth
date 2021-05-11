plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
    kotlin("kapt")
    id("realm-android")
}

android {
    compileSdkVersion(Sdk.COMPILE_SDK_VERSION)

    defaultConfig {
        minSdkVersion(Sdk.MIN_SDK_VERSION)
        targetSdkVersion(Sdk.TARGET_SDK_VERSION)

        applicationId = AppCoordinates.APP_ID
        versionCode = AppCoordinates.APP_VERSION_CODE
        versionName = AppCoordinates.APP_VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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


    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/*.kotlin_module")
    }

}

dependencies {
    implementation("androidx.room:room-runtime:2.2.5")
    kapt("androidx.room:room-compiler:2.2.5")
    work()
    implementation(kotlin("stdlib-jdk7"))
    
    implementation(project(":smooth-constraints"))
    implementation(project(":smooth-constraints-android"))
    implementation(project(":smooth-network"))
    implementation(project(":smooth-battery"))

    implementation(project(":smooth-use-cases"))
    implementation(project(":smooth-use-cases-android"))

    implementation(project(":smooth-data"))
    implementation(project(":smooth-data-store"))
    implementation(project(":smooth-store-memory"))
    implementation(project(":smooth-store"))
    implementation(project(":smooth-store-gson"))
    implementation(project(":smooth-store-room"))
    implementation(project(":smooth-store-json-room"))

    implementation(SupportLibs.ANDROIDX_APPCOMPAT)
    implementation(SupportLibs.ANDROIDX_CONSTRAINT_LAYOUT)
    implementation(SupportLibs.ANDROIDX_CORE_KTX)

//    testImplementation(Testing.JUNIT_4)

  //  androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_EXT_JUNIT)
  //  androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_RULES)
  //  androidTestImplementation(AndroidTestingLib.ESPRESSO_CORE)

    implementation(DI.DAGGER)
    kapt(DI.DAGGER_COMPILER)

    implementation(Reactive.RX_JAVA)
    implementation(Reactive.RX_ANDROID)
    implementation(Disposer.DISPOSER2)

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    implementation("androidx.startup:startup-runtime:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
    implementation("com.google.guava:guava:27.0.1-android")

    implementation("com.google.code.gson:gson:2.8.6")
    hilt()
}
