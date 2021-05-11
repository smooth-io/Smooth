include(":smooth-constraints")
include(":smooth-redux")
pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.android.library") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
            if (requested.id.id == "com.android.application") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        jcenter()
    }
}

rootProject.name = ("kotlin-android-template")

include(
    "app",
    "smooth-redux",
    "smooth-redux-use-cases",
    "smooth-constraints",
    "smooth-constraints-ktx",
    "smooth-constraints-android",
    "smooth-constraints-work",
    "smooth-store",
    "smooth-store-memory",
    "smooth-store-realm",
    "smooth-store-room",
    "smooth-store-gson",
    "smooth-store-json-room",
    "smooth-network",
    "smooth-network-ktx",
    "smooth-battery",
    "smooth-battery-ktx",
    "smooth-data",
    "smooth-data-store",
    "smooth-session",
    "smooth-use-cases",
    "smooth-use-cases-android",
    "smooth-deep-link"
)
