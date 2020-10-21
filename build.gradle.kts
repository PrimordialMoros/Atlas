allprojects {
    group = "me.moros"
    version = "1.0.0-SNAPSHOT"
    extra["isReleaseVersion"] = !version.toString().endsWith("SNAPSHOT")

    apply<JavaPlugin>()

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks {
        withType<AbstractArchiveTask> {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
        }
        withType<Sign>().configureEach {
            onlyIf { project.extra["isReleaseVersion"] as Boolean }
        }
    }
}

subprojects {
    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}
