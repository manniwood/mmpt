# How to build

You will need Gradle installed to build the jar file from source.

Once gradle is installed, all you will need to do is:

```Shell
./gradlew clean jar sourcesJar
```

To create the maven artifacts:

```Shell
./gradlew uploadArchives
```
which will upload them up to BinTray.

You will need the username and BinTray API Key in ~/.gradle/gradle.properties

## Testing

TODO: describe test database requred

