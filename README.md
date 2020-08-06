

# UniBench

## Research background, schema, and publications


As more businesses realized that data, in all forms and sizes, is critical to making the best possible decisions, we see the continued growth of systems that support the massive volume of relational or non-relational forms of data. Unlike traditional database management systems which are organized around a single data model that determines how data can be organized, stored, and manipulated, a multi-model database is designed to support multiple data models against a single, integrated backend. For example, document, graph, relational, and key-value models are examples of data models that may be supported by a multi-model database. 

The primary goal of UniBench project is to define a framework where different multi-model processing technologies can be fairly tested and compared, that can drive the identification of different systems' bottlenecks and required functionalities, and can help researchers open new frontiers in the fields on heterogeneous data management and integration.


We provide the ready-for-use datasets, which classified into intuitive “T-shirt” sizes (e.g., S, M, L, XL).



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
