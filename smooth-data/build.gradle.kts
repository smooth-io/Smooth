plugins {
    id("java-library")
    kotlin("jvm")
}

defaultDependencies {
    implementation(project(":smooth-use-cases"))
    implementation(project(":smooth-constraints"))
    implementation(project(":smooth-store"))
    implementation(project(":smooth-store-memory"))
    implementation(Ktx.FLOW)
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnitPlatform()
}
