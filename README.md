

# UniBench

## Research background, schema, and publications

... can be found on [our group website](https://www.helsinki.fi/en/researchgroups/unified-database-management-systems-udbms/unibench-towards-benchmarking-multi-model-dbms).

## Running UniBench

Instructions can be found in [the documentation](https://jiahenglu.github.io/UniBench2/). We provide a Docker image for your convenience.

## About this repository

This repository holds the source code of the UniBench dataset generator. It consists of three parts:

* `Preprocessing` which processes [DBpedia RDF graph](https://wiki.dbpedia.org/downloads-2016-10) as drop-in replacements of [seed datasets](https://github.com/jiahenglu/UniBench2/tree/master/ldbc_snb_datagen/src/main/resources/dictionaries) used by LDBC-SNB Data Generator,
* [`LDBC-SNB Data Generator`](https://github.com/ldbc/ldbc_snb_datagen) that uses DBpedia data as input, and
* `UniBench` that employs the output of the other two components to output ready-to-import multi-model datasets.

### Preprocessing

This component is developed on OpenJDK 11. It consists of several short source codes which transform DBpedia RDF to formats accepted by LDBC-SNB.

NB: `LDBC-SNB` and `UniBench` in the repo is already using the transformed datasets as input. You do not need to run `Preprocessing` by yourself.

### LDBC-SNB Data Generator & UniBench

These components are developed on OpenJDK 8. `LDBC-SNB Data Generator` uses Maven to manage its dependencies, while `UniBench` uses sbt.

## License

![https://www.gnu.org/graphics/gplv3-with-text-136x68.png](https://www.gnu.org/graphics/gplv3-with-text-136x68.png)
