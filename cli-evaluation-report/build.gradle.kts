plugins {
    id("org.jetbrains.kotlin.jvm") version Versions.kotlin
    application
}

val myMavenRepoReadUrl: String by project

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    maven {
        url = uri(myMavenRepoReadUrl)
    }
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.guava:guava:30.1.1-jre")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.4")
    implementation("com.github.ajalt:mordant:1.2.1")

    implementation(project(":texas-holdem-nl"))

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    // Define the main class for the application.
    mainClass.set("org.agrfesta.k.kards.texasholdem.cli.evaluationreport.AppKt")
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "org.agrfesta.k.kards.texasholdem.cli.evaluationreport.AppKt"
    }
}
