plugins {
	kotlin("jvm") version "2.1.20"
	kotlin("plugin.spring") version "2.1.20"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
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
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	// Kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	// Kotlin Coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.10.1")
	// Kotlin Reactor
//	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.3")

	// Tests
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
