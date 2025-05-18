plugins {
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.4"
    java
}

group = "com.demo"
version = "0.0.1-SNAPSHOT"
description = "Aerztekasse Coding Challenge"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

extra["springdocOpenapiVersion"] = "2.8.8"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${property("springdocOpenapiVersion")}")

    runtimeOnly("com.h2database:h2")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jar {
    archiveBaseName.set("aerztekasse")
    archiveVersion.set("")
}

tasks.bootJar {
    archiveBaseName.set("aerztekasse")
    archiveVersion.set("")
}
