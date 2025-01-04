import gradle.kotlin.dsl.accessors._8edd1b0c1852f0ac869e9c414c462ba9.mavenPublishing
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.vanniktech.maven.publish")
}

allprojects {
    group = "io.github.ronjunevaldoz"
    version = "1.0.2-dev07"
}

mavenPublishing {
    coordinates(
        groupId = "io.github.ronjunevaldoz",
        artifactId = "paymongo-kotlin",
        version = "1.0.2-dev07"
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
    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    // Enable GPG signing for all publications
    signAllPublications()
}