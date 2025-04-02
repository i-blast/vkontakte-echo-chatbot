plugins {
	kotlin("jvm") version "2.1.20"
	kotlin("plugin.spring") version "2.1.20"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.flywaydb.flyway") version "11.4.0"
}

group = "com.pii.bot"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa") // just in case you never know

	// Kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.10.1")

	// DB
	runtimeOnly("org.postgresql:r2dbc-postgresql:1.0.7.RELEASE")
	runtimeOnly("org.postgresql:postgresql:42.7.5") // for Flyway
	implementation("org.flywaydb:flyway-core:11.4.0")
	implementation("org.flywaydb:flyway-database-postgresql:11.4.0")
	implementation("org.hibernate:hibernate-core:6.6.12.Final") // just in case you never know

	// Elasticsearch
//	implementation("org.springframework.data:spring-data-elasticsearch")
//	implementation("co.elastic.clients:elasticsearch-java:8.17.4")

	// Tests
	testImplementation("org.testcontainers:junit-jupiter:1.20.6")
	testImplementation("org.testcontainers:postgresql:1.20.6")
	testImplementation("org.testcontainers:r2dbc:1.20.6")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		exclude(group = "org.mockito")
	}
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
	testImplementation("io.projectreactor:reactor-test")

	testImplementation("io.mockk:mockk:1.13.17")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
	testImplementation("com.squareup.okhttp3:mockwebserver:5.0.0-alpha.14")

	testImplementation(kotlin("test"))
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
    jvmArgs(
        "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED"
    )
}

flyway {
	url = "jdbc:postgresql://localhost:5432/message_db"
	user = "message"
	password = "message"
	schemas = arrayOf("vk_chat")
}
