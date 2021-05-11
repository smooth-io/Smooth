import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.*

private fun java(configure: org.gradle.api.Action<org.gradle.api.plugins.JavaPluginExtension>): kotlin.Unit { /* compiled code */
}

fun Project.kotlinProject(version: String) {
    this.version = version
    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

fun Project.defaultDependencies(configuration: DependencyHandlerScope.() -> Unit) =
    DependencyHandlerScope.of(dependencies).apply {
        this.dependencies.apply {
            implementation(kotlin("stdlib-jdk8"))
            implementation(Ktx.COROUTINE)
            implementation(Ktx.FLOW)
            testImplementation(Ktx.COROUTINE_TEST)
            test()
        }
        configuration()
    }

