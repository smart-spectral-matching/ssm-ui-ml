import json
import io
import os
import pickle
import psycopg2
import sys
import urllib

from ssm_ml import ssmml
from matplotlib.colors import ListedColormap

import numpy as np
import matplotlib.pyplot as plt

'''
A script to perform machine learning workflows.

usage:

ssm.py train (comma separated list of collection UUIDs to use in training) (json representation of the filter) (name to save the model under) (identifier string for type of classifier to train) (url for the database to read from) (human readable description of the filter)

or

ssm.py predict (name of model to predict with) (json description of the collection whose class will be predicted)

'''



if sys.argv[1] == "train":

    # Get the list of all datasets
    collection_uuids = sys.argv[2].split(',')

    fuseki_url = sys.argv[6]
    global_uuid = "curies"

    # All the collections from the database
    collections = []

    # Get each collection and label axes appropriately
    for uuid in collection_uuids:

        # TODO: Fix the TrainingView to drop the "datasets/" from uuid to use below...
        # url = sys.argv[6] + "/api/collections/" + global_uuid + "/datasets/" + uuid
        url = sys.argv[6] + "/api/collections/" + global_uuid + "/" + uuid
        req = urllib.request.Request(url)
        req.add_header('Authorization', 'Bearer ' + sys.argv[8])
        collection_url = urllib.request.urlopen(req, timeout=30)
        collection = json.loads(collection_url.read().decode())
        collections.append(collection)

    filter = ssmml.construct_filters(json.loads(sys.argv[3]))[0]


    # Get the labels for each collection
    labels = filter.getLabels(collections)

    # Get the Features for each collection, normalized in the range 0-1
    features = filter.getFeatures(collections)

    # List of extrema values for each feature
    extrema = []

    # Calculate the extrema for each feature
    for feature in features:

        new_extrema = [feature[0], feature[0]]

        for value in feature:
            if value < new_extrema[0]:
                new_extrema[0] = value
            elif value > new_extrema[1]:
                new_extrema[1] = value

        extrema.append(new_extrema)

    features = ssmml.normalize_features(features)

    classifier = ssmml.train(labels, features, sys.argv[4])
    ssmml.save_model(classifier,
                     sys.argv[5],
                     collection_uuids,
                     sys.argv[3],
                     extrema,
                     classifier.classes_,
                     sys.argv[7],
                     os.environ["ML_DATABASE_HOST"],
                     os.environ["ML_DATABASE_PORT"],
                     os.environ["ML_DATABASE_NAME"],
                     os.environ["ML_DATABASE_USER"],
                     os.environ["ML_DATABASE_PASSWORD"],
                     filter,
                     features,
                     labels)


    #print("Model " + sys.argv[5] + " trained and saved to database.")

elif sys.argv[1] == 'predict':

    # Load the filter's information out of the database
    filter_json = json.loads(
        ssmml.load_filter(
            sys.argv[2],
            os.environ["ML_DATABASE_HOST"],
            os.environ["ML_DATABASE_PORT"],
            os.environ["ML_DATABASE_NAME"],
            os.environ["ML_DATABASE_USER"],
            os.environ["ML_DATABASE_PASSWORD"]
        )
    )

    filters = ssmml.construct_filters(filter_json)

    # List of features of new data point
    features = []

    # Wrap the json-converter service output in a "scidata" node
    #full_json = [{'scidata' : json.loads(sys.argv[3])}]
    full_json = [json.loads(sys.argv[3])]

    # Convert all spectra to numbers if they're strings
    """
    for dataseries in full_json[0]['scidata']['dataseries']:
        for i, value in enumerate(dataseries[next(iter(dataseries))]['parameter']['numericValueArray'][0]['numberArray']):
            dataseries[next(iter(dataseries))]['parameter']['numericValueArray'][0]['numberArray'][i] = float(value)
    """

    labels, prob = ssmml.predict(filters, full_json, sys.argv[2], os.environ["ML_DATABASE_HOST"], os.environ["ML_DATABASE_PORT"],
                     os.environ["ML_DATABASE_NAME"], os.environ["ML_DATABASE_USER"], os.environ["ML_DATABASE_PASSWORD"])


    # Output label and probability for the first class
    print("Sample is of class '" + str(labels[0]) + "' with confidence " + str(prob[0] * 100) + "%")
