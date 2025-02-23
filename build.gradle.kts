plugins {
	java
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation ("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor ("org.mapstruct:mapstruct-processor:1.5.5.Final")
	compileOnly ("org.projectlombok:lombok:1.18.36")
	annotationProcessor ("org.projectlombok:lombok:1.18.36")
	testCompileOnly ("org.projectlombok:lombok:1.18.36")
	testAnnotationProcessor ("org.projectlombok:lombok:1.18.36")
	implementation("com.turkraft.springfilter:jpa:3.1.7")
	implementation ("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation ("com.nimbusds:nimbus-jose-jwt:9.31")
	implementation ("org.springframework.boot:spring-boot-starter-security")
	implementation ("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("mysql:mysql-connector-java:8.0.30")
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}


