### How I run the Features tutorial
`gradlew csv2shape -Dinput=/projects/geotools-tutorials/data/locations.csv -Doutput=/projects/geotools-tutorials/data/output-feature-tutorial/locations.shp`

To run the debugger and use breakpoints, add `--debug-jvm`

### Printed to the terminal

`INFO: dataFileCache open start`

`TYPE:SimpleFeatureTypeImpl Location identified extends Feature(the_geom:the_geom,name:name,number:number)`
`Header: LAT       ,LON        ,CITY       ,NUMBER`
`SHAPE:SimpleFeatureTypeImpl locations identified extends pointFeature(the_geom:Point,name:name,number:number)`

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
