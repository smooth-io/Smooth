plugins {
    id("java-library")
    id("kotlin")
    `maven-publish`
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