import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

version = LibraryKotlinCoordinates.LIBRARY_VERSION

plugins {
    id("java-library")
    kotlin("jvm")
    id("maven-publish")
}

defaultDependencies {
    api(project(":smooth-constraints"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
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