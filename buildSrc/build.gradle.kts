plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.19.0")
}
