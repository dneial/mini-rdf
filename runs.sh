for i in {1..30}
do
    /home/e20170009949/.jdks/openjdk-21.0.1/bin/java -jar RDFEngine.jar -d ./data/data/500K.rdf -q ./distinctQueries.queryset -o output/
    /home/e20170009949/.jdks/openjdk-21.0.1/bin/java -jar RDFEngine.jar -d ./data/data/500K.rdf -q ./duplicateQueries.queryset -o output/
done
