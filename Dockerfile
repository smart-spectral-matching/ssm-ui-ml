FROM openjdk:20-slim

EXPOSE 8082

ARG PYTHON_VERSION="3.8.20"
ENV PROFILE=${PROFILE:-"dev"}

ENV ML_DATABASE_HOST="localhost"
ENV ML_DATABASE_PORT=5432
ENV ML_DATABASE_NAME="ssm"
ENV ML_DATABASE_USER="ssm"
ENV ML_DATABASE_PASSWORD="password"

RUN mkdir -p /usr/share/man/man1/

RUN mkdir ssm
WORKDIR /ssm

RUN apt-get update -y \
    && apt-get install -y \
        build-essential \
        ca-certificates-java \
        curl \
        libpq-dev \
        libncurses5-dev \
        libgdbm-dev \
        libnss3-dev \
        libssl-dev \
        libsqlite3-dev \
        libreadline-dev \
        libffi-dev \
        libbz2-dev \
        maven \
        nodejs \
        npm \
        python3-dev \
        python3-pip \
        python-is-python3 \
        wget \
        zlib1g-dev \
    && rm -rf /var/lib/apt/lists/*

#RUN apk add --update --no-cache python3 && ln -sf python3 /usr/bin/python
#RUN python3 -m ensurepip
RUN rm -d -rf /usr/share/doc/openjdk-17-jre-headless
RUN apt update -y && apt upgrade -y && apt dist-upgrade -y
RUN apt install -y python3.9-venv
RUN mkdir /venv
RUN python3 -m venv /ssm/venv
#RUN pip3 install ssm-ml --extra-index-url https://${GITLAB_USERNAME}:${GITLAB_PASSWORD}@code.ornl.gov/api/v4/projects/7791/packages/pypi/simple
#COPY ssm_ml-0.1.0.tar.gz ./
RUN wget https://github.com/smart-spectral-matching/ssm-service-ml/archive/refs/tags/v0.1.0.tar.gz \
    && tar -xvzf v0.1.0.tar.gz \
    && cd ssm-service-ml-0.1.0 \
    && python -m pip install .
#RUN python3 -m pip install matplotlib notebook numpy psycopg2 scikit-learn 

ADD . ./

RUN mvn dependency:go-offline

CMD [ "sh", "-c", "mvn spring-boot:run -e -P$PROFILE" ]
