plugins {
    id("java")
    id("project-report")
    alias(libs.plugins.jmh)
}

group = "dev.johanness"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

/* JDK20
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
//JDK20 */

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.jetbrains.annotations)
    testCompileOnly(libs.jetbrains.annotations)
}

jmh {
    //includes = listOf("EnhancedForHelper")

    //timeOnIteration = "5s"
    //iterations = 5
    //warmup = "5s"
    //warmupIterations = 5
    //fork = 2
    //profilers = listOf("gc")

    /* JDK20
    jvmArgsAppend = listOf("--enable-preview")
    //JDK20 */
}
