import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`

plugins {
    `maven-publish`
    signing
}

publishing {
    // Configure all publications
    publications.withType<MavenPublication> {
        // Stub javadoc.jar artifact
        artifact(tasks.register("${name}JavadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
            archiveAppendix.set(this@withType.name)
        })

        // Provide artifacts information required by Maven Central
        pom {
            name.set("KotlinPaymongo")
            description.set("Paymongo Kotlin Client")
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
                    organization.set("Ron June Valdoz") // TODO update real org
                    organizationUrl.set("https://github.com/ronjunevaldoz") // TODO update real org url
                }
            }
        }
    }
}

signing {
    if (project.hasProperty("signing.gnupg.keyName")) {
        useGpgCmd()
        sign(publishing.publications)
    }
}
