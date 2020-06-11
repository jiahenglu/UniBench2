FROM iflavoursbv/mvn-sbt-openjdk-8-alpine:3.3.9_0.13.12

ENV JAVA_OPTS "-Xmx8g"

COPY ldbc_snb_datagen /code/ldbc_snb_datagen
COPY UniBench /code/UniBench
COPY gen.sh /code

RUN apk --update add --no-cache libc6-compat

# loading
RUN cd /code/ldbc_snb_datagen && mvn compile
RUN cd /code/UniBench && sbt compile

WORKDIR /code

CMD ["sh", "gen.sh"]