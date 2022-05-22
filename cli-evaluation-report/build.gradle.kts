plugins {
    id("org.agrfesta.k.kards.kotlin-cli-application-conventions")
}

dependencies {
    implementation(project(":playing-cards"))
    implementation(project(":texas-holdem-nl"))
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
