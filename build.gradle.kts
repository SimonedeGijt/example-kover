import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
    idea
//    jacoco
    id("org.jetbrains.kotlinx.kover") version "0.6.0"
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
            jvmTarget = JavaVersion.VERSION_17.toString()
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
    }

    kover {
        isDisabled.set(false) // default; put to true to disable instrumentation and all Kover tasks in this project
        engine.set(kotlinx.kover.api.DefaultIntellijEngine) // default; other engine option is Jacoco

        // you can override these filters on a few different levels
        // but as I don't really see the additional value doing that, I'm not including it
        // please view Kover Github if you feel you need this functionality
        filters {
            classes {
//            includes += "com.example.*"
                excludes += listOf("*Test*", "*Application*")
            }
        }

        instrumentation {
            excludeTasks += "dummy-tests" // set of test tasks names to exclude from instrumentation.
            // The results of their execution will not be presented in the report
        }

        xmlReport {
            onCheck.set(true) // true to run koverHtmlReport task during the execution of the check task (if it exists) of the current project
            reportFile.set(layout.buildDirectory.file("utrechtJUG/result.xml")) // change report file name
        }

        htmlReport {
            onCheck.set(true) // true to run koverHtmlReport task during the execution of the check task (if it exists) of the current project
            reportDir.set(layout.buildDirectory.dir("utrechtJUG/html-result")) // change report directory
        }

        verify {
            onCheck.set(true) // true to run koverVerify task during the execution of the check task (if it exists) of the current project

            rule {
                name = "Minimal branch coverage rate in percent" // optional custom name
                isEnabled = true // default
                target =
                    kotlinx.kover.api.VerificationTarget.ALL // specify by which entity the code for separate coverage evaluation will be grouped
                bound {
                    minValue = 100
                    counter = kotlinx.kover.api.CounterType.BRANCH // default LINE
                    valueType = kotlinx.kover.api.VerificationValueType.COVERED_PERCENTAGE // default
                }
            }
            rule {
                name = "Minimal line coverage rate in percent"

                bound {
                    minValue = 100
                    counter = kotlinx.kover.api.CounterType.LINE // default
                    valueType = kotlinx.kover.api.VerificationValueType.COVERED_PERCENTAGE // default
                }
            }
        }
    }
}
