package org.geotools.tutorial.solr;

import static org.geotools.data.solr.SolrDataStoreFactory.FIELD;
import static org.geotools.data.solr.SolrDataStoreFactory.URL;

import java.io.IOException;
import java.util.Map;
import org.geotools.data.solr.SolrDataStore;
import org.geotools.data.solr.SolrDataStoreFactory;
import org.geotools.data.solr.SolrLayerConfiguration;

public class SolrTutorial {

  public static void main(String[] args) throws IOException {

    SolrDataStoreFactory dataStoreFactory = new SolrDataStoreFactory();
    SolrDataStore dataStore =
        (SolrDataStore)
            dataStoreFactory.createDataStore(
                Map.of(URL.key, "http://localhost:8080/solr/geotools", FIELD.key, "foo"));

    Map<String, SolrLayerConfiguration> config = dataStore.getSolrConfigurations();
  }
}
