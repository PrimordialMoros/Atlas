plugins {
    `maven-publish`
    id("com.github.johnrengelman.shadow").version("7.0.0")
}

repositories {
    maven("https://repo.aikar.co/content/groups/aikar/")
}

dependencies {
    implementation("me.moros", "storage", "1.0.0")
    implementation("org.checkerframework", "checker-qual","3.12.0")
    implementation("com.github.ben-manes.caffeine", "caffeine", "3.0.1") {
        exclude(module = "error_prone_annotations")
        exclude(module = "checker-qual")
    }
    implementation("org.spongepowered", "configurate-hocon", "4.0.0") {
        exclude(module = "checker-qual")
    }
    implementation("org.jdbi", "jdbi3-core", "3.20.0") {
        exclude(module = "caffeine")
        exclude(module = "slf4j-api")
    }
    implementation("com.zaxxer", "HikariCP", "4.0.3") {
        exclude(module = "slf4j-api")
    }
    implementation("org.postgresql", "postgresql", "42.2.18")
    implementation("com.h2database", "h2", "1.4.200")
    implementation("co.aikar", "taskchain-bukkit", "3.7.2")
    implementation("co.aikar","acf-paper", "0.5.0-SNAPSHOT")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        dependencies {
            relocate("org.checkerframework", "me.moros.atlas.cf")
            relocate("org.spongepowered.configurate", "me.moros.atlas.configurate")
            relocate("com.typesafe", "me.moros.atlas.typesafe")
            relocate("co.aikar.taskchain", "me.moros.atlas.taskchain")
            relocate("co.aikar.commands", "me.moros.atlas.acf")
            relocate("co.aikar.locales", "me.moros.atlas.locales")
            relocate("com.zaxxer.hikari", "me.moros.atlas.hikari")
            relocate("org.jdbi", "me.moros.atlas.jdbi")
            relocate("org.postgresql", "me.moros.atlas.postgresql")
            relocate("org.h2", "me.moros.atlas.h2")
            relocate("io.leangen", "me.moros.atlas.leangen")
            relocate("org.antlr", "me.moros.atlas.antlr")
            relocate("com.github.benmanes.caffeine", "me.moros.atlas.caffeine")
        }
    }
    build {
        dependsOn(shadowJar)
    }
}
publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
    if (project.hasProperty("ossrhUsername") && project.hasProperty("ossrhPassword")) {
        repositories {
            maven {
                credentials {
                    username = project.property("ossrhUsername") as String?
                    password = project.property("ossrhPassword") as String?
                }
                name = "Snapshot"
                url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
            }
        }
    }
}
