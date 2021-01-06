plugins {
    id("com.github.johnrengelman.shadow").version("6.1.0")
}

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation(project(path = ":atlas-core", configuration = "shadow"))
    compileOnly("com.destroystokyo.paper", "paper-api", "1.16.4-R0.1-SNAPSHOT")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }
    build {
        dependsOn(shadowJar)
    }
    named<Copy>("processResources") {
        filesMatching("plugin.yml") {
            expand("pluginVersion" to project.version)
        }
    }
}
