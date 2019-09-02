### Query Lab

Query
A Filter is similar to the where clause of an SQL statement; defining a condition that each feature needs to meet in order to be selected.

Here is our strategy for displaying the selected features:

Get the feature type name selected by the user and retrieve the corresponding FeatureSource from the DataStore.
Get the query condition that was entered in the text field and use the CQL class to create a Filter object.
Pass the filter to the getFeatures method which returns the features matching the query as a FeatureCollection.
Create a FeatureCollectionTableModel for our dialog’s JTable. This GeoTools class takes a FeatureCollection and retrieves the feature attribute names and the data for each feature.