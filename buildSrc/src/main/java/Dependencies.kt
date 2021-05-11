import Versions.COUNTRY_PICKER_VERSION
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.*

object Sdk {
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 29
    const val COMPILE_SDK_VERSION = 29
}

object Versions {
    const val ANDROIDX_TEST_EXT = "1.1.1"
    const val ANDROIDX_TEST = "1.2.0"
    const val APPCOMPAT = "1.1.0"
    const val CONSTRAINT_LAYOUT = "1.1.3"
    const val CORE_KTX = "1.2.0"
    const val ESPRESSO_CORE = "3.2.0"
    const val JUNIT = "4.13"
    const val KTLINT = "0.37.2"
    const val LIFE_CYCLE = "2.2.0"
    const val SYSTEM_UI = "1.0.0"
    const val H_TEXT_VIEW = "0.1.6"
    const val GLIDE = "4.11.0"
    const val EXO = "2.11.4"
    const val BILLING = "2.1.0"
    const val RETROFIT = "2.6.4"
    const val ROOM = "2.2.6"
    const val DAGGER = "2.27"
    const val TIMBER = "4.7.1"
    const val COUNTRY_PICKER_VERSION = "0.2"

    const val JUNIT_5_VERSION = "5.7.0"
    const val MOCKK_VERSION = "1.10.3-jdk8"
    const val WORK_MANAGER_VERSION = "2.4.0"
    const val STARTUP = "1.0.0"

    const val HILT_VERSION = "2.33-beta"
    const val MOSHI_VERSION = "1.12.0"

    const val REDUX_VERSION = "0.5.5"
}


object BuildPluginsVersion {
    const val AGP = "4.1.3"
    const val DETEKT = "1.10.0"
    const val KOTLIN = "1.4.21"
    const val KTLINT = "9.2.1"
    const val VERSIONS_PLUGIN = "0.28.0"
}

object DI {
    const val JAVAX_INJECT = "javax.inject:javax.inject:1"

    const val HILT_CLASS_PATH =
        "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT_VERSION}"
    const val HILT_IMPL = "com.google.dagger:hilt-android:${Versions.HILT_VERSION}"
    const val HILT_KAPT = "com.google.dagger:hilt-compiler${Versions.HILT_VERSION}"

    const val DAGGER = "com.google.dagger:dagger:${Versions.DAGGER}"
    const val DAGGER_COMPILER = "com.google.dagger:dagger-compiler:${Versions.DAGGER}"
}

object SupportLibs {
    const val ANDROIDX_APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val ANDROIDX_CONSTRAINT_LAYOUT =
        "com.android.support.constraint:constraint-layout:${Versions.CONSTRAINT_LAYOUT}"
    const val ANDROIDX_CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
}

object Testing {
    const val JUNIT_4 = "junit:junit:${Versions.JUNIT}"
    const val JUNIT_5_JUPITER = "org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT_5_VERSION}"
    const val JUNIT_5_VINTAGE = "org.junit.vintage:junit-vintage-engine:${Versions.JUNIT_5_VERSION}"
    const val STRIKT = "io.strikt:strikt-core:0.28.1"
    const val MOCKK = "io.mockk:mockk:${Versions.MOCKK_VERSION}"
    const val MOCKK_DSL = "io.mockk:mockk-dsl-jvm:${Versions.MOCKK_VERSION}"


    const val MOCKITO_CORE = "org.mockito:mockito-core:3.1.0"
    const val MOCKITO_KOTLIN = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"
    const val TRUTH = "com.google.truth:truth:1.0.1"
    const val JUNIT_TEST = "androidx.test.ext:junit:1.1.1"
    const val ESPRESSO = "androidx.test.espresso:espresso-core:3.2.0"
}

object Ktx {
    const val COROUTINE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2"
    const val COROUTINE_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2"
    const val FLOW = "org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.4.3"
}

object AndroidTestingLib {
    const val ANDROIDX_TEST_RULES = "androidx.test:rules:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_EXT_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST_EXT}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
}

object Reactive {
    const val RX_ANDROID = "io.reactivex.rxjava2:rxandroid:2.1.1"
    const val RX_JAVA = "io.reactivex.rxjava2:rxjava:2.2.16"
    const val RX_JAVA_3 = "io.reactivex.rxjava3:rxjava:3.0.0-RC1"
}

object Lifecycle {
    const val VIEW_MODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFE_CYCLE}"
    const val RUN_TIME = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFE_CYCLE}"
    const val LIVE_DATA = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFE_CYCLE}"
    const val EXTENSTIONS = "androidx.lifecycle:lifecycle-extensions:${Versions.LIFE_CYCLE}"
}

object SystemUi {
    const val SYSTEM_UI = "io.getsmooth.kt.android:system_ui:${Versions.SYSTEM_UI}"
    const val SYSTEM_UI_LIFECYCLE =
        "io.getsmooth.kt.android:system_ui.lifecycle:${Versions.SYSTEM_UI}"
}

object UiDesign {
    const val MATERIAL_DESIGN = "com.google.android.material:material:1.3.0-alpha01"
    const val CONSTRAIN_LAYOUT = "androidx.constraintlayout:constraintlayout:1.1.3"
    const val MATERIAL_DIALOGS = "com.afollestad.material-dialogs:core:3.3.0"
    const val MATERIAL_DIALOGS_DATE_TIME = "com.afollestad.material-dialogs:datetime:3.3.0"
}

object Animation {
    const val VIEW_ANIMATOR = "com.github.florent37:viewanimator:1.1.0"
    const val SHIMMER = "com.facebook.shimmer:shimmer:0.5.0"
    const val CONSTRAIN_LAYOUT_BETA4 = "androidx.constraintlayout:constraintlayout:2.0.0-beta4"
    const val FADING_TEXT_VIEW = "com.tomer:fadingtextview:2.5"
    const val SHINE_BUTTON = "com.sackcentury:shinebutton:1.0.0"
    const val EASINGINTERPOLTOR = "com.daasuu:EasingInterpolator:1.3.0"
    const val LOTTIE = "com.airbnb.android:lottie:3.3.1"
    const val H_TEXT_VIEW =
        "com.hanks:htextview-base:${Versions.H_TEXT_VIEW}"        // base library
    const val H_TEXT_VIEW_SCAL = "com.hanks:htextview-scale:${Versions.H_TEXT_VIEW}"       // scale
}

object Images {
    const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:${Versions.GLIDE}"
}

object Helper {
    //    def splittiesVersion = "3.0.0-alpha06"
    const val SPLITTIES_MAIN_HANDLER = "com.louiscad.splitties:splitties-mainhandler:3.0.0-alpha06"
    const val SMART_ADAPTER = "io.github.manneohlund:smart-recycler-adapter:4.1.0"
    const val LOCAL_HELPER = "com.zeugmasolutions.localehelper:locale-helper-android:1.0.2"
    const val ANDROID_BROWSER_HELPER = "com.google.androidbrowserhelper:androidbrowserhelper:1.1.0"
    const val SWIPE_BACK = "com.github.liuguangqiang.swipeback:library:1.0.2@aar"
    const val JUST_FIED = "com.uncopt:android.justified:1.0"
    const val GOGWEN_SWIPE_BACK = "com.gongwen:swipeback:1.0.2"
}

object Firebase {
    const val CONFIG = "com.google.firebase:firebase-config-ktx:19.2.0"
    const val DYNAMIC_LINKS = "com.google.firebase:firebase-dynamic-links:19.1.0" //Remote-config
    const val MESSAGE = "com.google.firebase:firebase-messaging:20.2.4" //messaging
    const val ANALYTICS = "com.google.firebase:firebase-analytics:17.5.0"
    const val PREF = "com.google.firebase:firebase-perf:19.0.8"
    const val CRASHLAYTICS = "com.google.firebase:firebase-crashlytics:17.2.1"
    const val IN_APP_MESSAGING = "com.google.firebase:firebase-inappmessaging-display:19.1.0"
}

object Audio {
    const val EXO_CORE = "com.google.android.exoplayer:exoplayer-core:${Versions.EXO}"
    const val EXO_DASH = "com.google.android.exoplayer:exoplayer-dash:${Versions.EXO}"
    const val EXO_SMOOTH_STREAMING =
        "com.google.android.exoplayer:exoplayer-smoothstreaming:${Versions.EXO}"
    const val EXO_UI = "com.google.android.exoplayer:exoplayer-ui:${Versions.EXO}"
}

object Billing {
    const val BILLING = "com.android.billingclient:billing:${Versions.BILLING}"
    const val BILLING_KTX = "com.android.billingclient:billing-ktx:${Versions.BILLING}"

}

object Core {
    const val PLAY_CORE = "com.google.android.play:core:1.7.3"
    const val PLAY_CORE_KTX = "com.google.android.play:core-ktx:1.7.0"
    const val RECORDING = "com.smartlook.recording:app:1.4.2-native"
}

object Auth {
    //Auth providers
    const val FACEBOOK_SDK = "com.facebook.android:facebook-android-sdk:[5,6)"
    const val SERVICE_AUTH = "com.google.android.gms:play-services-auth:18.0.0"
}

object WorkManager {
    const val RUNTIME = "androidx.work:work-runtime:${Versions.WORK_MANAGER_VERSION}"
    const val KTX = "androidx.work:work-runtime-ktx:${Versions.WORK_MANAGER_VERSION}"
    const val RXJAVA = "androidx.work:work-rxjava2:${Versions.WORK_MANAGER_VERSION}"
    const val GCM = "androidx.work:work-gcm:${Versions.WORK_MANAGER_VERSION}"
    const val TEST = "androidx.work:work-testing:${Versions.WORK_MANAGER_VERSION}"
}

object RemoteApi {
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val JACKSON = "com.squareup.retrofit2:converter-jackson:${Versions.RETROFIT}"
    const val JACKSON_KOTLIN = "com.fasterxml.jackson.module:jackson-module-kotlin:2.10.2"
    const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:3.12.0"
}

object Database {

    object Room {
        const val RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
        const val COMPILE = "androidx.room:room-compiler:${Versions.ROOM}"
        const val KTX = "androidx.room:room-ktx:${Versions.ROOM}"
        const val TEST = "androidx.room:room-testing:${Versions.ROOM}"
    }

}

object Disposer {
    const val DISPOSER = "io.sellmair:disposer:2.0.0"
    const val DISPOSER2 = "io.sellmair:disposer:1.1.0"
}

object Startup {
    const val RUNTIME = "androidx.startup:startup-runtime:${Versions.STARTUP}"
}


object Logging {
    const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"
}

object SegmentAnalytics {
    const val SEGMENT = "com.segment.analytics.android:analytics:4.8.2"
    const val SEGMENT_FIREBASE = "com.segment.analytics.android.integrations:firebase:2.1.1@aar"
}

object Storage {
    const val CACHE_LITE = "io.github.kezhenxu94:cache-lite:0.2.0"
}

object CountryPicker {
    const val COUNTRY_PICKER =
        "com.hendraanggrian.appcompat:countrypicker:${COUNTRY_PICKER_VERSION}"
    const val COUNTRY_PIKER_SHEET =
        "com.hendraanggrian.appcompat:countrypicker-sheet:${COUNTRY_PICKER_VERSION}"
}


object Json {
    const val MOSHI = "com.squareup.moshi:moshi:${Versions.MOSHI_VERSION}"
    const val MOSHI_KOTLIN = "com.squareup.moshi:moshi-kotlin:${Versions.MOSHI_VERSION}"
    const val MOSHI_KOTLIN_CODE_GEN = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.MOSHI_VERSION}"
}

object Kotlin{
    const val REFLECT = "org.jetbrains.kotlin:kotlin-reflect:1.5.0"
}

object Redux{
    const val JVM = "org.reduxkotlin:redux-kotlin-threadsafe-jvm:${Versions.REDUX_VERSION}"
}
