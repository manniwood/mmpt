# How to build

You will need Gradle installed to build the jar file from source.

Once gradle is installed, all you will need to do is:

```
./gradlew jar

To create the maven artifacts:

```
./gradlew uploadArchives
```
which will make a little local mvn repo at /tmp/myRepo.

Copy that directory structure to the mvn directory
of your web server, and you will be good to go.

## Testing

TODO: describe test database requred

