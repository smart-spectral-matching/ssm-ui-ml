# SSM Machine Learning UI

Machine Learning UI for Smart Spectral Matching project

| Branch | Build | Coverage |
|--------|-------|----------|
| DEV    | [![DEV](https://code.ornl.gov/rse/datastreams/ssm/frontend/machine-learning-ui/badges/dev/pipeline.svg)](https://code.ornl.gov/rse/datastreams/ssm/frontend/machine-learning-ui/-/pipelines/dev/latest) | [![coverage report](https://code.ornl.gov/rse/datastreams/ssm/frontend/machine-learning-ui/badges/dev/coverage.svg)](https://code.ornl.gov/rse/datastreams/ssm/frontend/machine-learning-ui/-/commits/dev) |
| QA    | [![QA](https://code.ornl.gov/rse/datastreams/ssm/frontend/machine-learning-ui/badges/qa/pipeline.svg)](https://code.ornl.gov/rse/datastreams/ssm/frontend/machine-learning-ui/-/pipelines/qa/latest) | [![coverage report](https://code.ornl.gov/rse/datastreams/ssm/frontend/machine-learning-ui/badges/qa/coverage.svg)](https://code.ornl.gov/rse/datastreams/ssm/frontend/machine-learning-ui/-/commits/qa) |
| MAIN    | [![MAIN](https://code.ornl.gov/rse/datastreams/ssm/frontend/machine-learning-ui/badges/main/pipeline.svg)](https://code.ornl.gov/rse/datastreams/ssm/frontend/machine-learning-ui/-/pipelines/main/latest) | [![coverage report](https://code.ornl.gov/rse/datastreams/ssm/frontend/machine-learning-ui/badges/main/coverage.svg)](https://code.ornl.gov/rse/datastreams/ssm/frontend/machine-learning-ui/-/commits/main) |

# Installation

### Authenticating

Some dependencies are stored on private package registries on _code.ornl.gov_. A personal access token is required in order to install these with Pip.

Create a personal access token on the [Preferences > Access Tokens](https://code.ornl.gov/-/profile/personal_access_tokens) page with `read_api` or `api` permissions and save the auto-generated key. This key is only displayed immediately after generating it, so be sure to save it (otherwise a new token will need to be generated).

### Using pip to install `ssm-ml`

Install the private registry package, `ssm-ml`:
```
pip install ssm-ml --extra-index-url https://<token name>:<generated token>@code.ornl.gov/api/v4/projects/7791/packages/pypi/simple
```

### Using docker

We need to pass in the credentials for access to the private registry.

Run the following to build the container with the personal access token:
```
docker login code.ornl.gov:4567
docker build \
    --build-arg GITLAB_USERNAME=<token name> \
    --build-arg GITLAB_PASSWORD=<generated token> \
    -t ssm-ml-ui .
```
