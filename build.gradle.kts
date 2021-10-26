allprojects {
    group = "me.moros"
    version = "1.3.0-SNAPSHOT"

    apply<JavaPlugin>()

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(16))
    }

    tasks {
        withType<AbstractArchiveTask> {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
        }
    }
}

subprojects {
    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}
