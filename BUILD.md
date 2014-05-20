# How to build

You will need Gradle installed to build the jar file from source.

Once gradle is installed, all you will need to do is:

```
./gradlew jar

To create the maven artifacts:

```
./gradlew uploadArchives
```
which will upload them up to BinTray.

You will need the username and BinTray API Key in ~/.gradle/gradle.properties

## Testing

TODO: describe test database requred

