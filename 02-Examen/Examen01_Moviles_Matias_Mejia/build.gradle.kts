plugins {
    kotlin("jvm") version "2.0.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.simpleframework:simple-xml:2.7.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(15)
}


