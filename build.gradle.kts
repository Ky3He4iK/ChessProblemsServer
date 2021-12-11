import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
}

group = "dev.ky3he4ik"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}
//    <dependency>
//            <groupId>com.h2database</groupId>
//            <artifactId>h2</artifactId>
//            <scope>runtime</scope>
//        </dependency>
//
//        <dependency>
//            <groupId>io.springfox</groupId>
//            <artifactId>springfox-swagger2</artifactId>
//            <version>3.0.0</version>
//        </dependency>
//        <dependency>
//            <groupId>io.springfox</groupId>
//            <artifactId>springfox-boot-starter</artifactId>
//            <version>3.0.0</version>
//        </dependency>
//        <dependency>
//            <groupId>io.springfox</groupId>
//            <artifactId>springfox-boot-starter</artifactId>
//            <version>3.0.0</version>
//        </dependency>

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
