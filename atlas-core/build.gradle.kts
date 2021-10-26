plugins {
    `maven-publish`
    id("com.github.johnrengelman.shadow").version("7.1.0")
}

dependencies {
    implementation("me.moros", "storage", "2.0.0")
    implementation("com.github.ben-manes.caffeine", "caffeine", "3.0.4") {
        exclude(module = "error_prone_annotations")
        exclude(module = "checker-qual")
    }
    implementation("org.spongepowered", "configurate-hocon", "4.1.2") {
        exclude(module = "checker-qual")
    }
    implementation("org.jdbi", "jdbi3-core", "3.23.0") {
        exclude(module = "caffeine")
        exclude(module = "slf4j-api")
    }
    implementation("com.zaxxer", "HikariCP", "5.0.0") {
        exclude(module = "slf4j-api")
    }
    implementation("org.postgresql", "postgresql", "42.2.24") {
        exclude(module = "checker-qual")
    }
    implementation("com.h2database", "h2", "1.4.200")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        dependencies {
            relocate("org.spongepowered.configurate", "me.moros.atlas.configurate")
            relocate("com.typesafe", "me.moros.atlas.typesafe")
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
