plugins {
    id("org.agrfesta.k.kards.kotlin-common-conventions")
    application
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-cli:${Versions.kotlinxCli}")
    implementation("com.github.ajalt:mordant:${Versions.mordant}")
}
