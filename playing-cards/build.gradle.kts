import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.tasks.Jar
import io.gitlab.arturbosch.detekt.Detekt

plugins {
	id("org.jetbrains.kotlin.jvm") version Versions.kotlin
	id("org.jetbrains.dokka") version Versions.dokka
	id("io.gitlab.arturbosch.detekt") version Versions.detekt
	jacoco
    `java-library`
	`maven-publish`
}

val myMavenRepoWriteUrl: String by project

group = LibConfig.group
version = "1.1.0"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	gradlePluginPortal()
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	testImplementation(Dependencies.junitJupiterApi)
	testRuntimeOnly(Dependencies.junitJupiterEngine)
	testImplementation(Dependencies.assertk)
	testImplementation(Dependencies.mockk)
}

tasks.dokkaHtml {
	outputDirectory.set(buildDir.resolve("javadoc"))
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
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

tasks.withType<Detekt>().configureEach {
	reports {
		html.required.set(true)
		html.outputLocation.set(file("$buildDir/reports/detekt/deteckt.html"))
	}
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