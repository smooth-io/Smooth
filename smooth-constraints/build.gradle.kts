plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

defaultDependencies {
    implementation(DI.JAVAX_INJECT)
    implementation(project(":smooth-store"))
    implementation(project(":smooth-store-memory"))
}