FROM debian:buster-slim

ARG GITLAB_PASSWORD
ENV GITLAB_PASSWORD=$GITLAB_PASSWORD

RUN mkdir -p /usr/share/man/man1/
RUN apt-get update 
RUN apt-get install -y build-essential libpq-dev maven nodejs npm python3-pip python3-dev 
RUN pip3 install -U pip
RUN pip3 install matplotlib notebook numpy psycopg2 sklearn 
RUN pip3 install ssm-ml --extra-index-url https://__token__:$GITLAB_PASSWORD@code.ornl.gov/api/v4/projects/7791/packages/pypi/simple

ADD . ./

CMD [ "mvn", "spring-boot:run", "-Pdev" ]
