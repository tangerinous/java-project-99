# NOTE: No official gradle image with jdk20
FROM eclipse-temurin:20-jdk

ARG GRADLE_VERSION=8.2

RUN apt-get update && apt-get install -yq make unzip ca-certificates gnupg lsb-release

RUN wget -q https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle-${GRADLE_VERSION}-bin.zip \
    && rm gradle-${GRADLE_VERSION}-bin.zip

ENV GRADLE_HOME=/opt/gradle

RUN mv gradle-${GRADLE_VERSION} ${GRADLE_HOME}

ENV PATH=$PATH:$GRADLE_HOME/bin

# NOTE: install docker if anyone wants to use Testcontainer in test
RUN mkdir -p /etc/apt/keyrings
RUN curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg
RUN echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null
RUN apt-get update && apt-get install -yq docker-ce docker-ce-cli containerd.io docker-compose-plugin

WORKDIR /project
RUN mkdir /project/code

ENV GRADLE_USER_HOME /project/.gradle

COPY . .
