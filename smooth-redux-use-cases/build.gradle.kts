plugins {
    id("java-library")
    kotlin("jvm")
}

defaultDependencies {
    implementation(Redux.JVM)
    implementation(project(":smooth-redux"))
    implementation(project(":smooth-use-cases"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
