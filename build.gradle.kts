plugins {
    java
    signing
    `maven-publish`
    id("com.github.johnrengelman.shadow").version("6.0.0")
}

group = "me.moros"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
}

repositories {
	mavenCentral()
	maven("https://papermc.io/repo/repository/maven-public/")
	maven("https://oss.sonatype.org/content/repositories/snapshots")
	maven("https://repo.aikar.co/content/groups/aikar/")
}

dependencies {
    implementation("org.checkerframework", "checker-qual","3.7.0")
    implementation("com.github.ben-manes.caffeine", "caffeine", "2.8.5") {
        exclude(module = "error_prone_annotations")
        exclude(module = "checker-qual")
    }
    implementation("net.kyori", "adventure-platform-bukkit", "4.0.0-SNAPSHOT") {
        exclude(module = "checker-qual")
    }
    implementation("org.spongepowered", "configurate-hocon", "3.7.1") {
        exclude(module = "checker-qual")
        exclude(module = "guava")
        exclude(module = "guice")
    }
    implementation("org.jdbi", "jdbi3-core", "3.14.4") {
        exclude(module = "caffeine")
        exclude(module = "slf4j-api")
    }
    implementation("com.zaxxer", "HikariCP", "3.4.5") {
        exclude(module = "slf4j-api")
    }
    implementation("org.postgresql", "postgresql", "42.2.16.jre7")
    implementation("com.h2database", "h2", "1.4.200")
    implementation("co.aikar", "taskchain-bukkit", "3.7.2")
    implementation("co.aikar","acf-paper", "0.5.0-SNAPSHOT")
    implementation("net.jodah", "expiringmap", "0.5.9")
    compileOnly("com.destroystokyo.paper", "paper-api", "1.16.3-R0.1-SNAPSHOT")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set(rootProject.name)
        dependencies {
            relocate("org.checkerframework", "me.moros.atlas.checker")
            relocate("com.zaxxer", "me.moros.atlas.internal.hikari")
            relocate("net.kyori", "me.moros.atlas.kyori")
            relocate("ninja.leaping", "me.moros.atlas.configurate")
            relocate("com.typesafe", "me.moros.atlas.internal.typesafe")
            relocate("co.aikar.taskchain", "me.moros.atlas.taskchain")
            relocate("co.aikar.commands", "me.moros.atlas.acf")
            relocate("co.aikar.locales", "me.moros.atlas.internal.locales")
            relocate("org.jdbi", "me.moros.atlas.internal.jdbi")
            relocate("org.postgresql", "me.moros.atlas.internal.h2")
            relocate("org.h2", "me.moros.atlas.internal.postgresql")
            relocate("io.leangen", "me.moros.atlas.internal.jdbi-leangen")
            relocate("org.antlr", "me.moros.atlas.internal.jdbi-antlr")
            relocate("com.github.benmanes", "me.moros.atlas.internal.caffeine")
            relocate("net.jodah", "me.moros.atlas.internal.expiringmap")
        }
    }
    build {
        dependsOn(shadowJar)
    }
    withType<AbstractArchiveTask> {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    withType<Sign>().configureEach {
        onlyIf { !version.toString().endsWith("SNAPSHOT") }
    }
    named<Copy>("processResources") {
        filesMatching("plugin.yml") {
            expand("pluginVersion" to project.version)
        }
    }
}
publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
    repositories {
        maven {
            credentials {
                username = property("ossrhUsername") as String?
                password = property("ossrhPassword") as String?
            }
            name = "Snapshot"
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }
}
signing {
    sign(publishing.publications["maven"])
}
