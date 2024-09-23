import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`
import java.util.Properties

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
                    organization.set("Ron June Valdoz") // TODO update real org
                    organizationUrl.set("https://github.com/ronjunevaldoz") // TODO update real org url
                }
            }
        }
    }
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

    if (project.hasProperty("signing.gnupg.keyName")) {
        useGpgCmd()
        sign(publishing.publications)
    }
}
