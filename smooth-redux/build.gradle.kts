plugins {
    id("java-library")
    kotlin("jvm")
    `maven-publish`
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

version = "0.0.1"

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets.main.get().allSource)
}

publishing {
//    repositories {
//        maven {
//             change to point to your repo, e.g. http://my.org/repo
//            url = uri("$buildDir/repo")
//        }
//    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}
