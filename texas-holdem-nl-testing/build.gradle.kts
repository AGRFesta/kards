plugins {
    id("org.agrfesta.k.kards.kotlin-library-conventions")
}

group = LibConfig.group
version = LibConfig.version

dependencies {
    implementation(project(":playing-cards"))
    implementation(project(":texas-holdem-nl"))
}

