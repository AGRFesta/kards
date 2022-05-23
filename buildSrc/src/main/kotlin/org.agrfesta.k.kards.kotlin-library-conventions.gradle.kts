import org.gradle.jvm.tasks.Jar

plugins {
    id("org.agrfesta.k.kards.kotlin-common-conventions")
    id("org.jetbrains.dokka")
    `java-library`
    `maven-publish`
}

val myMavenRepoWriteUrl: String by project

tasks.dokkaHtml {
    outputDirectory.set(buildDir.resolve("javadoc"))
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc)
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["java"])
            artifact(dokkaJar)
            artifact(sourcesJar)
        }
    }
    repositories {
        maven {
            url = uri(myMavenRepoWriteUrl)
        }
    }
}