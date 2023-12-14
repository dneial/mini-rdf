for i in {1..30}
do
    java -jar RDFEngine.jar -d ./data/data/500K.nt -q ./distinctQueries.queryset -o output/
    java -jar RDFEngine.jar -d ./data/data/500K.nt -q ./duplicateQueries.queryset -o output/
done
