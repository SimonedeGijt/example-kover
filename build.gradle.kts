import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
    idea
//    jacoco
    id("org.jetbrains.kotlinx.kover") version "0.5.1"
    id("com.softeq.gradle.itest") version "1.0.4"
    id("com.github.ben-manes.versions") version "0.42.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

configurations {
    itestImplementation.get().extendsFrom(testImplementation.get())
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:${property("springWebVersion")}")

    testImplementation("org.assertj:assertj-core:${property("assertJVersion")}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${property("springWebVersion")}")
    testImplementation("org.testcontainers:junit-jupiter:${property("testcontainersVersion")}")
}

idea {
    module {
        inheritOutputDirs = false

        testSourceDirs.plusAssign(sourceSets["itest"].allSource.srcDirs)
        testResourceDirs.plusAssign(project.sourceSets["itest"].resources.srcDirs)
    }
}

fun setupEnv(task: Test) {
    val env = System.getenv("ENV") ?: "test"
    task.environment("SMOKE_TEST_ENV", env)
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }

//    withType<JacocoReport> {
//        dependsOn(test, integrationTest)
//        executionData(fileTree("$buildDir/jacoco/").include("**/*.exec"))
//    }

    withType<Test> {
        useJUnitPlatform {
            excludeTags("smoke")
        }

//        extensions.configure(kotlinx.kover.api.KoverTaskExtension::class) {
//            isDisabled = false // Instrumentation classes
// //            binaryReportFile.set(file("$buildDir/custom/result.bin")) // has an issue; see https://github.com/Kotlin/kotlinx-kover/issues/152
//            includes = listOf("org.example.*")
//            excludes = listOf("*Application*")
//        }
    }

//    koverHtmlReport {
//        includes = listOf("org.example.*")
//        excludes = listOf("*Test*", "*Application*")
//    }
//
//    check.get().dependsOn(koverHtmlReport)

    /**
     * The plugin currently only supports line counter values.
     * For future improvements, see https://github.com/Kotlin/kotlinx-kover/issues/128
     */
//    koverVerify {
//        includes = listOf("org.example.*")
//        excludes = listOf("*Test*", "*Application*")
//
//        rule {
//            name = "Minimal line coverage rate in percent"
//            bound {
//                minValue = 100
//            }
//        }
//    }
}

// kover {
//    isDisabled = false // Instrumentation classes
//    coverageEngine.set(kotlinx.kover.api.CoverageEngine.INTELLIJ) // change instrumentation agent and reporter
//    generateReportOnCheck = false // false to do not execute `koverMergedReport` task before `check` task
//    disabledProjects =
//        setOf() // setOf("project-name") to disable coverage for project with name `project-name`
//    instrumentAndroidPackage = false // true to instrument packages `android.*` and `com.android.*`
//    runAllTestsForProjectTask =
//        false // true to run all tests in all projects if `koverHtmlReport`, `koverXmlReport`, `koverReport`, `koverVerify` or `check` tasks executed on some project
// }
