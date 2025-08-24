plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "ru.topbun"
version = "0.0.1"

application {
    mainClass = "ru.topbun.ApplicationKt"
}

dependencies {


    implementation("io.github.cdimascio:dotenv-kotlin:6.4.2")
    implementation("mysql:mysql-connector-java:8.0.23")
    implementation("at.favre.lib:bcrypt:0.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
