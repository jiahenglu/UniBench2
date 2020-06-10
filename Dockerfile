FROM iflavoursbv/mvn-sbt-openjdk-8-alpine:3.3.9_0.13.12

ENV JAVA_OPTS "-Xmx8g"

COPY ldbc_snb_datagen /code/ldbc_snb_datagen
COPY UniBench /code/UniBench
COPY gen.sh /code

RUN apk --update add --no-cache libc6-compat

WORKDIR /code

CMD ["sh", "gen.sh"]