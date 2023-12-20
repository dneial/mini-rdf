for i in {1..15}
do
    for l in data/queries/10000/*.queryset;
    do
        /home/e20170009949/.jdks/openjdk-21.0.1/bin/java -jar RDFEngine.jar -d ./data/data/500K.nt -q $l -o output/template_comparison/
    done
done
