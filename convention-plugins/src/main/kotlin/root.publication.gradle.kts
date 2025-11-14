import gradle.kotlin.dsl.accessors._94cffe4e74c4f6a3b1c88c3e0c336ef5.mavenPublishing

plugins {
    id("com.vanniktech.maven.publish")
}

allprojects {
    group = "io.github.ronjunevaldoz"
    version = "1.0.3"
}

mavenPublishing {
    coordinates(
        groupId = "io.github.ronjunevaldoz",
        artifactId = "paymongo-kotlin",
        version = "1.0.3"
    )

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
                id.set("ronjunevaldoz")
                name.set("Ron June Valdoz")
                url.set("https://github.com/ronjunevaldoz/")
                email.set("ronjune.lopez@gmail.com")
                organization.set("Ron June Valdoz") // TODO update real org
                organizationUrl.set("https://github.com/ronjunevaldoz") // TODO update real org url
            }
        }
    }
}