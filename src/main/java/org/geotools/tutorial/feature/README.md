### Feature and Feature Type

A feature is something that can be drawn on a map. The strict definition is that a feature is something in the real world – a feature of the landscape - Mt Everest, the Eiffel Tower, or even something that moves around like your great aunt Alice.

Explaining the concept to Java developers is easy - a feature is an Object.

Like a java object features can contain some information about the real world thing that they represent. This information is organized into attributes just as in Java information is slotted into fields.

Occasionally you have two features that have a lot in common. You may have the LAX airport in Los Angeles and the SYD airport in Sydney. Because these two features have a couple of things in common it is nice to group them together - in Java we would create a Class called Airport. On a map we will create a Feature Type called Airport.

### How to run the Features tutorial
`gradlew csv2shape -Dinput=/projects/geotools-tutorials/data/locations.csv -Doutput=/projects/geotools-tutorials/data/output-feature-tutorial/locations.shp`

- The makes gradle build and run the tutorial.
- Substitute **input** for the absolute path to the `locations.csv` file.
- Substitute **output** for the absolute path to the `locations.shp` shape file.
- To run the debugger and use breakpoints, add `--debug-jvm`

### What gets printed to the transcript
### Printed to the terminal

`INFO: dataFileCache open start`

`TYPE:SimpleFeatureTypeImpl Location identified extends Feature(the_geom:the_geom,name:name,number:number)`

`Header: LAT       ,LON        ,CITY       ,NUMBER`

`SHAPE:SimpleFeatureTypeImpl locations identified extends pointFeature(the_geom:Point,name:name,number:number)`
