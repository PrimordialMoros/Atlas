allprojects {
    group = "me.moros"
    version = "1.0.0"
}

subprojects {
    repositories {
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.aikar.co/content/groups/aikar/")
    }
    buildDir = rootProject.buildDir
}
