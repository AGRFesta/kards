import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.tasks.Jar

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.61"
	id("org.jetbrains.dokka") version "0.10.0"
	jacoco
    `java-library`
	`maven-publish`
}

val myMavenRepoWriteUrl: String by project

group = "agrfesta.kcards"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	jcenter()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	testImplementation("org.junit.jupiter:junit-jupiter-api:5.1.1")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.1.1")
	testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.20")
	testImplementation("io.mockk:mockk:1.10.0")
}

tasks.dokka {    
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
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
	classifier = "javadoc"
	from(tasks.dokka)
}

publishing {
	publications {
		create<MavenPublication>("default") {
			from(components["java"])
			artifact(dokkaJar)
		}
	}
	repositories {
		maven {
			url = uri(myMavenRepoWriteUrl)
		}
	}
}