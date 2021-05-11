version = LibraryKotlinCoordinates.LIBRARY_VERSION

plugins {
    id("java-library")
    kotlin("jvm")
    id("maven-publish")
}

defaultDependencies {
    implementation(project(":smooth-store"))
    implementation(Ktx.FLOW)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
