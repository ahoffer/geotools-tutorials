package org.geotools.tutorial.feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * This example reads data for point locations and associated attributes from a comma separated text
 * (CSV) file and exports them as a new shapefile. It illustrates how to build a feature type.
 *
 * <p>Note: to keep things simple in the code below the input file should not have additional spaces
 * or tabs between fields.
 */
public class Csv2Shape {

  public static void main(String[] args) throws Exception {
    // Set cross-platform look & feel for compatibility
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

    // Let user choose a file
    File file = JFileDataStoreChooser.showOpenFile("csv", null);
    if (file == null) {
      return;
    }

    // Create FeatureType
    final SimpleFeatureType TYPE = createSimpleFeatureType();
    System.out.println("TYPE:" + TYPE);

    // GeometryFactory creates the geometry attribute a feature using a Point for the location.
    org.locationtech.jts.geom.GeometryFactory geometryFactory =
        JTSFactoryFinder.getGeometryFactory();
    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

    List<SimpleFeature> features = buildFeaturesFromCsvFile(file, geometryFactory, featureBuilder);
  }

  /* Read CSV file and create a feature for each record. Please note the following:
  Use  GeometryFactory to create new Points
  Creation of features (SimpleFeature objects) using SimpleFeatureBuilder
  */
  private static List<SimpleFeature> buildFeaturesFromCsvFile(
      File csvFile, GeometryFactory geometryFactory, SimpleFeatureBuilder featureBuilder)
      throws IOException {
    List<SimpleFeature> features = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
      /* First line of the data file is the header */
      String line = reader.readLine();
      System.out.println("Header: " + line);

      for (line = reader.readLine(); line != null; line = reader.readLine()) {
        if (line.trim().length() > 0) { // skip blank lines
          String tokens[] = line.split("\\,");

          double latitude = Double.parseDouble(tokens[0]);
          double longitude = Double.parseDouble(tokens[1]);
          String name = tokens[2].trim();
          int number = Integer.parseInt(tokens[3].trim());

          /* Longitude (= x coord) first ! */
          Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

          featureBuilder.add(point);
          featureBuilder.add(name);
          featureBuilder.add(number);
          SimpleFeature feature = featureBuilder.buildFeature(null);
          features.add(feature);
        }
      }
    }
    return features;
  }

  /*
   * We use the DataUtilities class to create a FeatureType that will describe the data in our
   * shapefile.
   *
   * See also the createFeatureType method below for another, more flexible approach.
   */
  private static SimpleFeatureType createSimpleFeatureType() throws SchemaException {
    return DataUtilities.createType(
        "Location",
        "the_geom:Point:srid=4326,"
            + // <- the geometry attribute: Point type
            "name:String,"
            + // <- a String attribute
            "number:Integer" // a number attribute
        );
  }
}
