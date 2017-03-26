FROM openjdk:8

ENV SCALA_VERSION 2.12.1
ENV SBT_VERSION 0.13.13
ENV NETTY_PORT 9000
ENV BROKER_PATH https://ec2-54-201-33-44.us-west-2.compute.amazonaws.com:8443

# Scala expects this file
RUN touch /usr/lib/jvm/java-8-openjdk-amd64/release

# Install Scala
## Piping curl directly in tar
RUN \
  curl -fsL http://downloads.typesafe.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz | tar xfz - -C /root/ && \
  echo >> /root/.bashrc && \
  echo 'export PATH=~/scala-$SCALA_VERSION/bin:$PATH' >> /root/.bashrc

# Install sbt
RUN \
  curl -L -o sbt-$SBT_VERSION.deb http://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt && \
  sbt sbtVersion

RUN mkdir -p /apps/
COPY ./ /apps/play-tenancy-filter/

EXPOSE $NETTY_PORT

WORKDIR /apps/play-tenancy-filter

ENTRYPOINT ["sbt"]
CMD ["run"]