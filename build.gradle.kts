import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.kotlin.dsl.kotlinScriptDefExtensions as withConvention

plugins {
	id("org.springframework.boot") version "2.5.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.20"
	kotlin("plugin.spring") version "1.5.21"
}

group = "com.johncooper"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
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
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.liquibase:liquibase-core")
	implementation("org.mariadb.jdbc:mariadb-java-client")

	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("org.testcontainers:testcontainers:1.16.0")
	testImplementation("org.testcontainers:junit-jupiter:1.16.0")
	testImplementation("org.testcontainers:mariadb:1.16.3")
	testImplementation("org.testcontainers:r2dbc:1.16.0")
//	testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
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

sourceSets {
	create("cucumber") {
		withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
			kotlin.srcDirs("src/cucumber/kotlin", "src/cucumber")
			resources.srcDir("src/cucumber/resources")
			compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
			runtimeClasspath += output + compileClasspath + sourceSets["test"].runtimeClasspath
		}
	}
}

task<Test>("cucumber") {
	description = "Runs the cucumber tests"
	group = "verification"
	testClassesDirs = sourceSets["cucumber"].output.classesDirs
	classpath = sourceSets["cucumber"].runtimeClasspath
	useJUnitPlatform()
}

//https://stackoverflow.com/questions/52904603/integration-tests-with-gradle-kotlin-dsl/52906232#52906232
