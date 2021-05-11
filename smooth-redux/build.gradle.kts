plugins {
    id("java-library")
    kotlin("jvm")
}

defaultDependencies {
    implementation(Redux.JVM)
    implementation(Kotlin.REFLECT)
    implementation(DI.JAVAX_INJECT)
    implementation(project(":smooth-store"))
    implementation(project(":smooth-store-memory"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
