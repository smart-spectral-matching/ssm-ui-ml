FROM openjdk:17-slim

EXPOSE 8080

ARG PYTHON_VERSION="3.8.20"
ENV PROFILE=${PROFILE:-"dev"}

RUN mkdir -p /usr/share/man/man1/

RUN mkdir ssm
WORKDIR /ssm

RUN apt-get update \
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
        zlib1g-dev \
    && rm -rf /var/lib/apt/lists/*

RUN python3 -m pip install -U pip
#RUN pip3 install ssm-ml --extra-index-url https://${GITLAB_USERNAME}:${GITLAB_PASSWORD}@code.ornl.gov/api/v4/projects/7791/packages/pypi/simple
COPY ssm_ml-0.1.0.tar.gz ./
RUN python3 -m pip install ./ssm_ml-0.1.0.tar.gz
#RUN python3 -m pip install matplotlib notebook numpy psycopg2 scikit-learn 

ADD . ./

RUN mvn dependency:go-offline

CMD [ "sh", "-c", "mvn spring-boot:run -P$PROFILE" ]
