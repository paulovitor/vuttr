import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
    id("org.springframework.boot") version "2.5.0-RC1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.0-RC"
    kotlin("kapt") version "1.5.0-RC"
    kotlin("plugin.spring") version "1.5.0-RC"
    id("org.openapi.generator") version "5.1.0"
    id("org.jmailen.kotlinter") version "3.4.3"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.5.0-RC"
}

group = "br.com.pvsoftware"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

buildscript {
    repositories {
        mavenLocal()
        maven { url = uri("https://repo1.maven.org/maven2") }
    }
    dependencies {
        classpath("org.openapitools:openapi-generator-gradle-plugin:5.0.0")
    }
}

extra["spring-security.version"] = "5.5.0-RC2"

val generatedSourcesDir = "$buildDir/generated/openapi"

tasks.compileJava {
    dependsOn(tasks.openApiGenerate)
}

tasks.compileKotlin {
    dependsOn(tasks.openApiGenerate)
}

openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/src/main/resources/openapi/api.yaml")
    outputDir.set(generatedSourcesDir)
    apiPackage.set("$group.api")
    modelPackage.set("$group.model")
    generateApiTests.set(false)
    generateModelTests.set(false)
    configOptions.set(
        mapOf(
            "dateLibrary" to "java8",
            "interfaceOnly" to "true",
            "reactive" to "true"
        )
    )
}

kotlinter {
    ignoreFailures = false
    reporters = arrayOf("checkstyle", "plain")
    experimentalRules = true
    disabledRules = arrayOf("parameter-list-wrapping", "indent")
}

tasks {
    "lintKotlinMain"(LintTask::class) {
        exclude("**/*ApiUtil.kt", "**/*ToolsApi.kt", "**/*Error.kt", "**/*Tool.kt", "**/*ToolBody.kt")
    }
}

sourceSets {
    getByName("main") {
        java {
            srcDir("$generatedSourcesDir/src/main/kotlin")
        }
    }
}

repositories {
    maven { url = uri("https://repo.spring.io/milestone") }
    mavenCentral()
}

dependencies {
    compileOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("org.mapstruct:mapstruct:1.4.2.Final")
    kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.springfox:springfox-boot-starter:3.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
