#!/bin/sh

echo
echo "Generating LDBC datasets..."
sleep 5

cd /code/ldbc_snb_datagen
rm -rf parameter_curation

mvn compile exec:java -Dexec.mainClass="ldbc.snb.datagen.generator.LDBCDatagen" -Dexec.args="ldbc_params.ini"


echo
echo "Generating UniBench datasets..."
sleep 5

cd /code/UniBench
rm -rf Unibench

sbt "runMain Unibench1_0"