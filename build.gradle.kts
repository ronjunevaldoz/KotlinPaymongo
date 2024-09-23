import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("maven-publish")
    id("signing")
    id("org.jetbrains.dokka") version "1.6.10"
}


val ktorVersion: String by project
val serializationVersion: String by project

group = "io.github.ronjunevaldoz"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Ktor client
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("io.ktor:ktor-client-auth:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                // Ktor serialization
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                // Kotlin serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
            }
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
            implementation(libs.kotlinx.coroutine.swing)
        }

        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
        val jvmTest by getting
//        val jsMain by getting
//        val jsTest by getting
//        val nativeMain by getting
//        val nativeTest by getting
    }
}


android {
    namespace = "com.ronjunevaldoz.paymongo"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}



val dokkaOutputDir = "$buildDir/dokka"

tasks.getByName<org.jetbrains.dokka.gradle.DokkaTask>("dokkaHtml") {
    outputDirectory.set(file(dokkaOutputDir))
}

val deleteDokkaOutputDir by tasks.register<Delete>("deleteDokkaOutputDirectory") {
    delete(dokkaOutputDir)
}

val javadocJar = tasks.register<Jar>("javadocJar") {
    dependsOn(deleteDokkaOutputDir, tasks.dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaOutputDir)
}


val properties = Properties().apply {
    load(rootProject.file("secret.properties").reader())
}

// <OSSRH jira account username>
val ossrhUsername = properties["ossrhUsername"] as String?
// <OSSRH jira account password>
val ossrhPassword  = properties["ossrhPassword"] as String?
// <your-key-id-(last 8 character of your key)>
val signingKeyId = properties["signing.keyId"] as String?
// passphrase
val signingPassword = properties["signing.password"] as String?
// gpg --export-secret-keys <last 8 character of your key> | base64
val signingKey = properties["signing.secretKey"] as String?
// gpg --edit-key F8F55D3E
// gpg --list-signatures
// gpg --keyring secring.gpg --export-secret-keys > ~/.gnupg/secring.gpg
// or raw base64 secretkey
// gpg --export-secret-keys <last 8 character of your key> | base64
// https://stackoverflow.com/a/39573795/2801777

signing {
    useInMemoryPgpKeys(
        signingKeyId,
        signingKey,
        signingPassword
    )
}

publishing {
    publications {
        repositories {
            maven {
                val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                url =  uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }
        }
        withType<MavenPublication> {
            artifact(javadocJar)
            pom {
                name.set("KotlinPaymongo")
                description.set("Paymongo client for kotlin")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                url.set("https://ronjunevaldoz.github.io/KotlinPaymongo")
                issueManagement {
                    system.set("Github")
                    url.set("https://github.com/ronjunevaldoz/KotlinPaymongo/issues")
                }
                scm {
                    connection.set("https://github.com/ronjunevaldoz/KotlinPaymongo.git")
                    url.set("https://github.com/ronjunevaldoz/KotlinPaymongo")
                }
                developers {
                    developer {
                        name.set("Ron June Valdoz")
                        email.set("ronjune.lopez@gmail.com")
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications)
}