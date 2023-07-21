plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.15.0"
}

group = "com.github.ivanlarios"
version = "1.2.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:marketplace-zip-signer:0.1.8")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.1")
    plugins.set(listOf("com.intellij.java"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    patchPluginXml {
        sinceBuild.set("231")
        untilBuild.set("232.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN") ?: File("./certs/chain.crt").readText(Charsets.UTF_8))
        privateKey.set(System.getenv("PRIVATE_KEY") ?: File("./certs/privateKey.pem").readText(Charsets.UTF_8))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    downloadZipSigner{
        version.set("0.1.8")
    }

    publishPlugin {
        channels.set(listOf(System.getenv("CHANNEL") ?: "Stable"))
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}


