import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.tasks.Jar
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
	id("org.jetbrains.dokka") version "1.5.31"
	id("io.gitlab.arturbosch.detekt").version("1.19.0")
	jacoco
    `java-library`
	`maven-publish`
}

val myMavenRepoWriteUrl: String by project

group = "org.agrfesta.k.kards"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	gradlePluginPortal()
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
	testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
	testImplementation("io.mockk:mockk:1.10.0")
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