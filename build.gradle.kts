import org.jetbrains.kotlin.descriptors.annotations.composeAnnotations
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun
import org.gradle.kotlin.dsl.*

plugins {
	id("org.springframework.boot") version "2.5.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.20"
	kotlin("plugin.spring") version "1.5.21"
	id("com.avast.gradle.docker-compose") version "0.15.2"
}

group = "com.johncooper"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven {
		url = uri("https://repo1.maven.org/maven2/")
	}
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

	testImplementation("io.rest-assured:rest-assured:5.0.1")
	implementation("io.rest-assured:json-path:5.0.1")
	implementation("io.rest-assured:xml-path:5.0.1")

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


val SourceSet.kotlin: SourceDirectorySet
	get() = project.extensions.getByType<KotlinJvmProjectExtension>().sourceSets.getByName(name).kotlin

sourceSets {
	create("cucumber") {
//		compileKotlin.destinationDir = compileJava.destinationDir
		kotlin.srcDirs( "src/cucumber")
		compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
		runtimeClasspath += output + compileClasspath + sourceSets["test"].runtimeClasspath
	}
}

val cucumber = task<Test>("cucumber") {
	description = "Runs the cucumber tests"
	group = "verification"
	testClassesDirs = sourceSets["cucumber"].output.classesDirs
	classpath = sourceSets["cucumber"].runtimeClasspath
	useJUnitPlatform()
	dependsOn("composeUp")
	finalizedBy("composeDown")
}

tasks.withType<Jar>() {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

//dockerCompose.isRequiredBy(cucumber)

configure<com.avast.gradle.dockercompose.ComposeExtension> {
	includeDependencies.set(true)
	isRequiredBy(cucumber)
	createNested("local").apply {
		projectName = "reactiveKotlin"
		environment.putAll(mapOf("TAGS" to "feature-test,local"))
		startedServices.set(listOf("myservice", "mariadb"))
		upAdditionalArgs.set(listOf("--no-deps"))
	}
}
