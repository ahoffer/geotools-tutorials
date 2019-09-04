package org.geotools.tutorial.solr;

import static org.geotools.data.solr.SolrDataStoreFactory.LAYER_MAPPER;
import static org.geotools.data.solr.SolrDataStoreFactory.URL;

import java.io.IOException;
import java.util.Map;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.solr.SolrDataStore;
import org.geotools.data.solr.SolrDataStoreFactory;
import org.geotools.data.solr.SolrFeatureSource;
import org.geotools.data.solr.SolrLayerMapper.Type;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.spatial.BBOX;

public class SolrTutorialTest {

  @Test
  public void test() throws IOException {

    SolrDataStoreFactory dataStoreFactory = new SolrDataStoreFactory();
    String layerName = Type.SINGLE.name();
    SolrDataStore dataStore =
        (SolrDataStore)
            dataStoreFactory.createDataStore(
                Map.of(
                    URL.key, "http://localhost:8080/solr/test_core", LAYER_MAPPER.key, layerName));

    // What is this? FeatureType names? ... No it is the name of the Solr cores.
    String[] typeNames = dataStore.getTypeNames();

    // Get source
    SolrFeatureSource featureSource = (SolrFeatureSource) dataStore.getFeatureSource(layerName);

    // Construct filter
    int SOURCE_SRID = 4326;
    FilterFactory ff = dataStore.getFilterFactory();
    BBOX bbox = ff.bbox("geo", -185, -98, 185, 98, "EPSG:" + SOURCE_SRID);

    // Get results fom Solr
    SimpleFeatureCollection features = featureSource.getFeatures(bbox);
  }
}
