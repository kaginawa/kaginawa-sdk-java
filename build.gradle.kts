plugins {
    `java-library`
    `maven-publish`
    signing
    jacoco
}

val junitVersion = "5.6.2"

group = "io.github.kaginawa"
version = "0.2.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("jakarta.json.bind:jakarta.json.bind-api:1.0.2")
    runtimeOnly("org.eclipse:yasson:1.0.8")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:3.4.6")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks {
    withType<JavaCompile> {
        options.compilerArgs = listOf("--module-path", classpath.asPath)
    }

    withType<Javadoc> {
        options.locale = "en_US"
        options.modulePath = classpath.toList()
        (options as StandardJavadocDocletOptions).apply {
            links("https://docs.oracle.com/en/java/javase/11/docs/api/")
        }
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
                developers {
                    developer {
                        id.set("mikan")
                        name.set("Yutaka Kato")
                        email.set("mikan@aomikan.org")
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
        maven {
            name = "OSSRH"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(System.getenv("SIGNING_KEY"), System.getenv("SIGNING_PASSWORD"))
    sign(publishing.publications)
}
