plugins {
    id("java")
    id("project-report")
    alias(libs.plugins.jmh)
}

group = "dev.johanness"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.jetbrains.annotations)
    testCompileOnly(libs.jetbrains.annotations)
}

jmh {
    //includes = ['ProxyCreation']
}
