package org.geotools.tutorial.feature;

import static java.lang.System.exit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * The tutorial covers creating a FeatureType, FeatureCollection and Features. It uses a
 * GeometryFactory to build Points, then writes out a Shapefile. We also force projection. We start
 * by building a shapefile from scratch so you get to see every last * thing that goes into creating
 * features. To keep things simple the values in the input file should not have additional spaces or
 * tabs between fields.
 */
public class Csv2Shape {

  public static void main(String[] args) throws Exception {

    // Tasks unrelated to GIS
    setUiLookAndFeel();
    File inputFile = getInputFile();

    // Create FeatureType
    final SimpleFeatureType TYPE = createSimpleFeatureType();
    System.err.println("TYPE:" + TYPE);

    // Create a collection of features using the data in the file.

    List<SimpleFeature> features = buildFeaturesFromCsvFile(inputFile, TYPE);

    // Get a file object to use for the new shape file.
    File outputFile = getOutputFile();

    // Create a datastore for the a new shape file.
    ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
    ShapefileDataStore newDataStore =
        (ShapefileDataStore)
            dataStoreFactory.createNewDataStore(createDatastoreParameters(outputFile));

    // Set the schema for the data store.
    newDataStore.createSchema(TYPE);

    // Start a transaction
    Transaction transaction = new DefaultTransaction("create");

    String typeName = newDataStore.getTypeNames()[0];
    SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);
    SimpleFeatureType SHAPE_TYPE = featureSource.getSchema();
    System.err.println("SHAPE:" + SHAPE_TYPE);

    SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
    SimpleFeatureCollection collection = new ListFeatureCollection(TYPE, features);
    featureStore.setTransaction(transaction);
    try {
      featureStore.addFeatures(collection);
      transaction.commit();
    } catch (Exception problem) {
      problem.printStackTrace();
      transaction.rollback();
    } finally {
      transaction.close();
    }
    return;
  }

  // -----------------------------------------------------------------------------------------------
  static void setUiLookAndFeel()
      throws ClassNotFoundException, InstantiationException, IllegalAccessException,
          UnsupportedLookAndFeelException {
    // Set cross-platform look & feel for compatibility
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
  }

  static Map<String, Serializable> createDatastoreParameters(File newFile)
      throws MalformedURLException {
    Map<String, Serializable> params = new HashMap<>();
    params.put("url", newFile.toURI().toURL());
    params.put("create spatial index", Boolean.TRUE);
    return params;
  }

  /* Read CSV file and create a feature for each record. Please note the following:
  Use  GeometryFactory to create new Points
  Creation of features (SimpleFeature objects) using SimpleFeatureBuilder
  */
  static List<SimpleFeature> buildFeaturesFromCsvFile(File csvFile, SimpleFeatureType featureType)
      throws IOException {
    org.locationtech.jts.geom.GeometryFactory geometryFactory =
        JTSFactoryFinder.getGeometryFactory();
    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
    List<SimpleFeature> features = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
      /* First line of the data file is the header */
      String line = reader.readLine();
      System.err.println("Header: " + line);

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
  static SimpleFeatureType createSimpleFeatureType() throws SchemaException {
    return DataUtilities.createType(
        "Location",
        "the_geom:Point:srid=4326,"
            + // <- the geometry attribute: Point type
            "name:String,"
            + // <- a String attribute
            "number:Integer" // a number attribute
        );
  }

  /**
   * Prompt the user for the name and path to use for the output shapefile
   *
   * @return name and path for the shapefile as a new File object
   */
  static File getNewShapeFile() {

    JFileDataStoreChooser chooser = new JFileDataStoreChooser("shp");
    chooser.setDialogTitle("Save shapefile");
    int returnVal = chooser.showSaveDialog(null);
    if (returnVal != JFileDataStoreChooser.APPROVE_OPTION) {
      // the user cancelled the dialog
      exit(0);
    }
    File newFile = chooser.getSelectedFile();
    return newFile;
  }

  static File getInputFile() {
    File inputFile = null;
    var input = System.getProperty("input");
    if (null == input) {
      // Let user choose a file
      inputFile = JFileDataStoreChooser.showOpenFile("csv", null);
    } else {
      // Use file passed in as command line argument
      inputFile = new File(input);
    }
    if (!inputFile.canRead()) {
      System.err.println("Cannot read from input file: " + input);
      exit(1);
    }
    return inputFile;
  }

  static File getOutputFile() {
    File outputFile = null;
    var output = System.getProperty("output");
    if (null == output) {
      outputFile = getNewShapeFile();
    } else {
      outputFile = new File(output);
      if (!outputFile.exists()) {
        System.err.println("Cannot write to output file: " + output);
        exit(1);
      }
    }
    return outputFile;
  }
}
