object Versions {
    const val kotlin = "1.6.10"
    const val dokka = "1.6.10"
    const val detekt = "1.19.0"
    const val kotlinxCollectionsImmutable = "0.3.5"
    const val playingCards = "1.1.0"
    const val jUnit = "5.8.2"
    const val assertk = "0.25"
    const val mockk = "1.12.3"
}

/**
 * To define plugins
 */
object BuildPlugins {
    val kotlin by lazy { "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}" }
}

/**
 * To define dependencies
 */
object Dependencies {
    val kotlinxCollectionsImmutable by lazy {
        "org.jetbrains.kotlinx:kotlinx-collections-immutable:${Versions.kotlinxCollectionsImmutable}" }
    val playingCards by lazy { "org.agrfesta.k.kards:k-playing-cards:${Versions.playingCards}" }
    val junitJupiterApi by lazy { "org.junit.jupiter:junit-jupiter-api:${Versions.jUnit}" }
    val junitJupiterEngine by lazy { "org.junit.jupiter:junit-jupiter-engine:${Versions.jUnit}" }
    val assertk by lazy { "com.willowtreeapps.assertk:assertk-jvm:${Versions.assertk}" }
    val mockk by lazy { "io.mockk:mockk:${Versions.mockk}" }
}
