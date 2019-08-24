SORT 2019
=========

Contains various demos used in my presentation at SORT 2019.

Project was generated using the [JCStress][1] archetype:

```
mvn archetype:generate \
 -DinteractiveMode=false \
 -DarchetypeGroupId=org.openjdk.jcstress \
 -DarchetypeArtifactId=jcstress-java-test-archetype \
 -DgroupId=io.github.drautb \
 -DartifactId=sort-2019 \
 -Dversion=1.0
```

Execute the tests:

```
java -jar target/jcstress.jar
```


[1]: https://wiki.openjdk.java.net/display/CodeTools/jcstress
