import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.21"
	kotlin("plugin.spring") version "1.5.21"
}

group = "com.johncooper"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

sourceSets.main {
	java.srcDirs("src/main")
}

sourceSets.test {
	java.srcDirs("src/test", "src/cucumber/kotlin", "src/cucumber")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.json:json:20211205")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.4")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.6.3")
	implementation("org.mariadb:r2dbc-mariadb")

	implementation("org.liquibase:liquibase-gradle-plugin:2.0.3")
	implementation("org.liquibase:liquibase-core:2.0.3")

	implementation("org.mariadb.jdbc:mariadb-java-client:3.0.3")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("org.testcontainers:testcontainers:1.16.0")
	testImplementation("org.testcontainers:junit-jupiter:1.16.0")
	testImplementation("org.testcontainers:mariadb:1.16.3")
	testImplementation("org.testcontainers:r2dbc:1.16.0")
	testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.junit.vintage:junit-vintage-engine:5.7.2")
	testImplementation("io.cucumber:cucumber-java:6.11.0")
	testImplementation("io.cucumber:cucumber-spring:6.11.0")
	testImplementation("io.cucumber:cucumber-junit:6.11.0")
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

tasks {
	test {
		filter {
			excludeTestsMatching("**.RunBDDTests")
		}
	}
}

task<Test>("acceptanceTest") {
	useJUnitPlatform()
	filter {
		includeTestsMatching("**.RunBDDTests")
	}
}