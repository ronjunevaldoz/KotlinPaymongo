//plugins {
//    id("org.jetbrains.dokka")
//}
//
//val dokkaOutputDir = "$buildDir/dokka"
//
//tasks.getByName<org.jetbrains.dokka.gradle.DokkaTask>("dokkaHtml") {
//    outputDirectory.set(file(dokkaOutputDir))
//}
//
//val deleteDokkaOutputDir by tasks.register<Delete>("deleteDokkaOutputDirectory") {
//    delete(dokkaOutputDir)
//}
//
//val javadocJar = tasks.register<Jar>("javadocJar") {
//    dependsOn(deleteDokkaOutputDir, tasks.dokkaHtml)
//    archiveClassifier.set("javadoc")
//    from(dokkaOutputDir)
//}