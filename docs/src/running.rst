Running UniBench
================

Build from source
-----------------

Before building and running UniBench from source, make sure you have installed all packages/software mentioned in the :ref:`label_prerequisites` section.

To build UniBench from source, follow the following steps:

1. Clone UniBench with Git and change the working directory:

..  code-block:: shell
    
    $ git clone https://github.com/jiahenglu/UniBench2.git
    $ cd UniBench2

2. Change parameters of ``ldbc_snb_datagen`` by editing ``ldbc_snb_datagen/ldbc_params.ini``. Refer to `the official wiki <https://github.com/ldbc/ldbc_snb_datagen/wiki/Configuration#generator-parameters>`_ for how to modify this file.

3. (Optional) Reduce the size of output of UniBench by decreasing the sampling probabilities (range: 0 to 1) in ``unibench_params.ini``.

3. Restore dependencies and compile UniBench:

..  code-block:: shell
    
    $ cd ldbc_snb_datagen
    $ mvn compile
    $ cd ../UniBench
    $ sbt compile

4. Finally, run ``ldbc_snb_datagen`` and then ``UniBench``:

..  code-block:: shell
    
    $ cd ../ldbc_snb_datagen
    $ mvn exec:java -Dexec.mainClass=ldbc.snb.datagen.generator.LDBCDatagen \
      -Dexec.args="ldbc_params.ini"
    $ cd ../UniBench
    $ sbt "runMain Unibench1_0"

5. Check ``ldbc_snb_datagen/parameter_curation`` and ``UniBench/Unibench`` folders to find the generated dataset. Note that names of files are not the same as the released datasets.

Run with Docker
---------------

Running UniBench could not be easier once you have `Docker <https://www.docker.com/>`_ installed. Simply execute the following command:


..  code-block:: shell
    
    $ docker run --rm -e JAVA_OPTS=-Xmx8g \
      -v "$(pwd)/ldbc:/code/ldbc_snb_datagen/parameter_curation" \
      -v "$(pwd)/unibench:/code/UniBench/Unibench" \
      jiahenglu/unibench

A few comments:

* When running on Windows, replace ``$(pwd)`` by the absolute path of the working directory.
* The statement ``JAVA_OPTS=-Xmx8g`` limits the maximum heap size of the JVM. It is 8GB by default, and we recommend users to set it to a reasonably large value (Note that you may have to first enlarge the CPU & RAM limit of Docker if you are using Windows or macOS).

After the execution has been completed, please check two new folders in the working directory, ``ldbc`` and ``unibench``, for generated datasets.


