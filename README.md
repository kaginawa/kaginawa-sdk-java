kagianwa-sdk-go
===============

[![Actions Status](https://github.com/kaginawa/kaginawa-sdk-java/workflows/Gradle/badge.svg)](https://github.com/kaginawa/kaginawa-sdk-java/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=kaginawa_kaginawa-sdk-java&metric=alert_status)](https://sonarcloud.io/dashboard?id=kaginawa_kaginawa-sdk-java)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=kaginawa_kaginawa-sdk-java&metric=coverage)](https://sonarcloud.io/dashboard?id=kaginawa_kaginawa-sdk-java)
[![Download](https://api.bintray.com/packages/kaginawa/kaginawa-sdk-java/kaginawa-sdk-java/images/download.svg)](https://bintray.com/kaginawa/kaginawa-sdk-java/kaginawa-sdk-java/_latestVersion)
[![javadoc](https://javadoc.io/badge2/com.github.kaginawa/kaginawa-sdk-java/javadoc.svg)](https://javadoc.io/doc/com.github.kaginawa/kaginawa-sdk-java)

The official [Kaginawa](https://github.com/kaginawa/kaginawa) SDK for the Java and other JVM languages.

## Prerequisites

- Java 11 or higher

## Importing

### Maven

Set the jcenter repository to the `repositories` element:

```xml
<repository>
    <id>central</id>
    <name>bintray</name>
    <url>https://jcenter.bintray.com</url>
</repository>
```

Add a dependency to the `dependencies` element:

```xml
<dependency>
    <groupId>com.github.kaginawa</groupId>
    <artifactId>kaginawa-sdk-java</artifactId>
    <version>0.0.1</version>
</dependency>
```

### Gradle

Set a repository to jcenter:

```kotlin
repositories {
    jcenter()
}
```

Add a dependency:

```kotlin
implementation("com.github.kaginawa:kaginawa-sdk-java:0.0.1")
```

## Documentation

Online javadoc is available on [javadoc.io](https://javadoc.io/doc/com.github.kaginawa/kaginawa-sdk-java).

## Examples

See [KaginawaHello.java](src/main/java/com/github/kaginawa/examples/hello/KaginawaHello.java).

## License

Kaginawa SDK for Java licensed under the [Apache License 2.0](LICENSE).

## Author

- [mikan](https://github.com/mikan)
