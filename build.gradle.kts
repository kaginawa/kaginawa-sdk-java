plugins {
    `java-library`
    `maven-publish`
    jacoco
    id("com.jfrog.bintray") version "1.8.5"
}

val junitVersion = "5.6.2"

group = "com.github.kaginawa"
version = "0.0.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.json.bind:javax.json.bind-api:1.0")
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

bintray {
    user = if (project.hasProperty("bintrayUser")) project.property("bintrayUser") as String else System.getenv("BINTRAY_USER")
    key = if (project.hasProperty("bintrayApiKey")) project.property("bintrayApiKey") as String else System.getenv("BINTRAY_API_KEY")
    setPublications("mavenJava")
    pkg.apply {
        repo = "kaginawa-sdk-java"
        name = "kaginawa-sdk-java"
        userOrg = "kaginawa"
        setLicenses("Apache-2.0")
        vcsUrl = "https://github.com/kaginawa/kaginawa-sdk-java.git"
    }
}
