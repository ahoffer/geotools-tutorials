# docker build -t solrjts:latest .
# docker run --mount source=geo,target=/var/solr/data/geo solrjts
# docker run -p 8983:8983 solrjts:latest
FROM solr:8.2
USER root
RUN wget -O /opt/solr/server/solr-webapp/webapp/WEB-INF/lib/jts-core-1.16.1.jar https://github.com/locationtech/jts/releases/download/jts-1.16.1/jts-core-1.16.1.jar
USER solr
COPY --chown=solr:solr core.properties /var/solr/data/geo/
COPY --chown=solr:solr conf /var/solr/data/geo/conf
EXPOSE 8983
