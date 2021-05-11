plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

defaultDependencies {
    implementation(Ktx.FLOW)
    implementation(project(":smooth-constraints"))
    implementation(project(":smooth-use-cases"))
    implementation(project(":smooth-store"))
}