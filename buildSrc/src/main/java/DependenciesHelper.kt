import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.dagger() {
    implementation(DI.DAGGER)
    kapt(DI.DAGGER_COMPILER)
}

fun DependencyHandler.test() {
    testImplementation(Testing.JUNIT_5_JUPITER)
    testImplementation(Testing.JUNIT_5_VINTAGE)
    testImplementation(Testing.STRIKT)
    testImplementation(Testing.MOCKK)
    testImplementation(Testing.MOCKK_DSL)
}

fun DependencyHandler.coroutine() {
    implementation(Ktx.COROUTINE)
    testImplementation(Ktx.COROUTINE_TEST)
}

fun DependencyHandler.room() {
    annotationProcessor(Database.Room.RUNTIME)
    implementation(Database.Room.COMPILE)
    testImplementation(Database.Room.TEST)
}

fun DependencyHandler.roomKtx() {
    implementation(Database.Room.KTX)
}

fun DependencyHandler.work() {
    implementation(WorkManager.RUNTIME)
    testImplementation(WorkManager.TEST)
}

fun DependencyHandler.workKtx() {
    implementation(WorkManager.KTX)
}

fun DependencyHandler.workRx() {
    implementation(WorkManager.RXJAVA)
}

fun DependencyHandler.startup() {
    implementation(Startup.RUNTIME)
}

fun DependencyHandler.hilt() {
    implementation(DI.HILT_IMPL)
    kapt(DI.HILT_KAPT)
}

fun DependencyHandler.moshi() {
    implementation(Json.MOSHI)
    kapt(Json.MOSHI_KOTLIN_CODE_GEN)
}