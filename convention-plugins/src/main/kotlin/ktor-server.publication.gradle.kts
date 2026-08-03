import gradle.kotlin.dsl.accessors._94cffe4e74c4f6a3b1c88c3e0c336ef5.mavenPublishing

plugins {
    id("com.vanniktech.maven.publish")
}

val libraryVersion = providers.gradleProperty("libraryVersion").get()

group = "io.github.ronjunevaldoz"
version = libraryVersion

mavenPublishing {
    coordinates(
        groupId = "io.github.ronjunevaldoz",
        artifactId = "paymongo-kotlin-ktor-server",
        version = libraryVersion
    )

    pom {
        name.set("KotlinPaymongo Ktor Server")
        description.set("Ktor server plugin for verifying and receiving PayMongo webhooks")
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
                id.set("ronjunevaldoz")
                name.set("Ron June Valdoz")
                url.set("https://github.com/ronjunevaldoz/")
                email.set("ronjune.lopez@gmail.com")
                organization.set("Ron June Valdoz")
                organizationUrl.set("https://github.com/ronjunevaldoz")
            }
        }
    }
}
