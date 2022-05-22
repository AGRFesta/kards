plugins {
    id("org.agrfesta.k.kards.kotlin-library-conventions")
}

group = LibConfig.group
version = LibConfig.version

dependencies {
    implementation(Dependencies.kotlinxCollectionsImmutable)

    implementation(project(":playing-cards"))

    testImplementation(project(":texas-holdem-nl-testing"))
}

