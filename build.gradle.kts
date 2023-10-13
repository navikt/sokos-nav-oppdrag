import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm") version "1.9.10"
}

val ktorVersion = "2.3.5"
val mockkVersion = "1.13.8"

allprojects {
    group = "no.nav.sokos"

    repositories {
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    }

    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {

        // Ktor server
        implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")

        // Test
        testImplementation("io.mockk:mockk:$mockkVersion")

    }
}

subprojects {

    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    tasks {

        withType<Test>().configureEach {
            useJUnitPlatform()

            testLogging {
                showExceptions = true
                showStackTraces = true
                exceptionFormat = TestExceptionFormat.FULL
                events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
            }

            reports.forEach { report -> report.required.value(false) }
        }

        withType<Wrapper>() {
            gradleVersion = "8.1"
        }
    }
}

tasks.jar {
    enabled = false
}
