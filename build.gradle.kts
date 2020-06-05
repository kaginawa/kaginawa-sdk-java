plugins {
    `java-library`
    `maven-publish`
    jacoco
}

val junitVersion = "5.6.2"

group = "com.github.kaginawa"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.json.bind:javax.json.bind-api:1.0")
    runtimeOnly("org.eclipse:yasson:1.0.7")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:3.3.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks {
    withType<JavaCompile> {
        options.compilerArgs = listOf("--module-path", classpath.asPath)
    }

    withType<Javadoc> {
        options.modulePath = classpath.toList()
    }

    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
        jvmArgs = listOf("--add-opens", "java.base/java.util=ALL-UNNAMED")
    }

    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.isEnabled = true
        }
    }
}

configurations {
    implementation {
        resolutionStrategy.failOnVersionConflict()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "kaginawa-sdk-java"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("Kaginawa SDK for Java")
                description.set("API client library for Kaginawa Server written in Java.")
                url.set("https://github.com/kaginawa")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/kaginawa/kaginawa-sdk-java.git")
                    developerConnection.set("scm:git:ssh://github.com/kaginawa/kaginawa-sdk-java.git")
                    url.set("https://github.com/kaginawa/kaginawa-sdk-java")
                }
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/kaginawa/kaginawa-sdk-java")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
