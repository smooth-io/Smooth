import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    this.add("testImplementation", dependencyNotation)


fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    this.add("implementation", dependencyNotation)

fun DependencyHandler.kapt(dependencyNotation: Any): Dependency? =
    add("kapt", dependencyNotation)

fun DependencyHandler.annotationProcessor(dependencyNotation: Any): Dependency? =
    add("annotationProcessor", dependencyNotation)
