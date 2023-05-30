FROM openjdk:17-slim

EXPOSE 8080

ARG GITLAB_USERNAME=$GITLAB_USERNAME
ARG GITLAB_PASSWORD=$GITLAB_PASSWORD

ENV PROFILE=${PROFILE}

RUN mkdir -p /usr/share/man/man1/
RUN apt-get update 
RUN apt-get install -y build-essential curl libpq-dev maven nodejs npm python3-pip python3-dev 
RUN pip3 install -U pip
RUN pip3 install matplotlib notebook numpy psycopg2 scikit-learn 
RUN pip3 install ssm-ml --extra-index-url https://${GITLAB_USERNAME}:${GITLAB_PASSWORD}@code.ornl.gov/api/v4/projects/7791/packages/pypi/simple

RUN mkdir ssm
WORKDIR /ssm
ADD . ./

RUN mvn dependency:go-offline

CMD [ "sh", "-c", "mvn spring-boot:run -P$PROFILE" ]
