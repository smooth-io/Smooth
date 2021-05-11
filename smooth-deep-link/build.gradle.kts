plugins {
    id("java-library")
    kotlin("jvm")
}

defaultDependencies {
    implementation(project(":smooth-constraints"))
    implementation(project(":smooth-store"))
    implementation(Ktx.FLOW)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnitPlatform()
}