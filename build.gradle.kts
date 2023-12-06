import me.champeau.jmh.JmhBytecodeGeneratorTask

plugins {
    id("java")
    id("project-report")
    alias(libs.plugins.jmh)
}

group = "dev.johanness"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_20
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
}
tasks.withType<Test>().configureEach {
    jvmArgs("--enable-preview")
}
tasks.withType<JavaExec>().configureEach {
    jvmArgs("--enable-preview")
}
tasks.withType<JmhBytecodeGeneratorTask>().configureEach {
    jvmArgs.add("--enable-preview")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.jetbrains.annotations)
    testCompileOnly(libs.jetbrains.annotations)
}

jmh {
    //includes = listOf("EnhancedForHelper")

    //timeOnIteration = "2s"
    //iterations = 5
    //warmup = "2s"
    //warmupIterations = 5
    //fork = 2

    jvmArgsAppend = listOf("--enable-preview")
}
