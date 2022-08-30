import com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask
import com.github.davidmc24.gradle.plugin.avro.ResolveAvroDependenciesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://packages.confluent.io/maven/")
        maven(url = "https://jitpack.io")
    }
}

plugins {
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"

    // avro
    id("com.github.davidmc24.gradle.plugin.avro") version "1.3.0"
    id("com.github.imflog.kafka-schema-registry-gradle-plugin") version "1.6.1"
}

group = "se.ohou"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    maven(url = "https://packages.confluent.io/maven/")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.11")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.11")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    // avro
    implementation("io.confluent:kafka-avro-serializer:7.2.1")
}

tasks {

    withType<KotlinCompile> {
        dependsOn("generateAvroJava", "generateTestAvroJava")
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    // avro
    register<ResolveAvroDependenciesTask>("resolveAvroDependencies") {
        source = fileTree("avro")
        setOutputDir(file("build/avro/resolved"))
    }

    withType<GenerateAvroJavaTask> {
        dependsOn("resolveAvroDependencies")
        source = fileTree("build/avro/resolved")
    }
}

schemaRegistry {
    url.set("http://localhost:8085")
    register {
        subject("TopicKey", "avro/topic-key.avsc")
        subject("TopicValue", "avro/topic-value.avsc")
    }
}