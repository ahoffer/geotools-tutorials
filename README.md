

### Install JAI core into local maven repository

`mvn install:install-file -Dfile="lib/jai_core-1.1.3.jar.zip" -DgroupId=javax.media -DartifactId=jai-core -Dversion=1.1.3 -Dpackaging=jar`


### Make fat JAR
Add this to gradle.build to create a fat JAR

```
jar {
    manifest {
        attributes "Main-Class": "org.geotools.tutorial.quickstart.Quickstart"
    }

    from {
        configurations.compile.collect { it.isDirectory() ? it : zipTree(it) }
    }
}
```
