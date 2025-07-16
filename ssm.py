import json
import io
import math
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
    
elif sys.argv[1] == 'match':
    
    data = json.loads(sys.argv[2])
    series = data["scidata"]["dataseries"]
    unknown = [series[0]["x-axis"]["parameter"]["numericValueArray"][0]["numberArray"], series[1]["y-axis"]["parameter"]["numericValueArray"][0]["numberArray"]]
    
    # Convert user file to floats
    for i in range(len(unknown[0])):
        unknown[0][i] = float(unknown[0][i])
        unknown[1][i] = float(unknown[1][i])
        
    
    fuseki_host = sys.argv[3]
    
    #Get the list of all models
    dataset_uuids = []
    #global_uuid = json.loads(urllib.request.urlopen(fuseki_host + "/api/datasets").read().decode())[-1]
    global_uuid = json.loads(urllib.request.urlopen(fuseki_host + "/api/collections").read().decode())[-1]

    #uuid_url = urllib.request.urlopen(fuseki_host + "/api/datasets/" + global_uuid + "/models/uuids")
    uuid_url = urllib.request.urlopen(fuseki_host + "/api/collections/" + global_uuid + "/datasets/uuids")
    dataset_uuids_json = uuid_url.read().decode()
    dataset_uuids = json.loads(dataset_uuids_json)
    
    #All the datasets from the database
    datasets = []
    
    #Series for each dataset in the database
    dataset_series = []
    
    #Get each dataset and label axes appropriately
    for uuid in dataset_uuids:
        dataset_url = urllib.request.urlopen(uuid.replace("10.64.198.148", "ssm-dev.ornl.gov:8080"))
        dataset = json.loads(dataset_url.read().decode())
        datasets.append(dataset)
        dataset_series.append([dataset["scidata"]["dataseries"][0]["x-axis"]["parameter"]["numericValueArray"][0]["numberArray"],
                              dataset["scidata"]["dataseries"][1]["y-axis"]["parameter"]["numericValueArray"][0]["numberArray"]])
    
    # Convert data series to floats
    for ds in dataset_series:
        for i in range(len(ds[0])):
            ds[0][i] = float(ds[0][i])
            ds[1][i] = float(ds[1][i])
    
    # Series after being cast into the right range for comparision with user file
    features = []
    
    # Normalize the user and database series into the same y-axis range
    y_axes = []
    y_axes.append(unknown[1])
    
    # For each data series, interpolate it into the same x axis as the user file
    for ds in dataset_series:
        feature = ssmml.interpolate_spectra(unknown, ds)
        features.append(feature)
        y_axes.append(feature[1])

    norm_features = ssmml.normalize_features(y_axes)

    unknown[1] = norm_features[0]
    
    for i in range(len(features)):
        features[i][1] = norm_features[i + 1]
    
    # Calculate the uned score between the user file and each data series
    uneds = []
    
    for i in range(len(dataset_series)):
        if i != 0:
            uneds.append(ssmml.unit_normalized_euclidean_distance(unknown, features[i]))
            
    # Get the lowest 5 uned scores' indices
    uned_indices = np.argsort(np.array(uneds))[:2]
    
    pces = []
    
    for i in range(len(dataset_series)):
        if i != 0:
            pces.append(ssmml.pearson_correlation_coefficient(unknown, features[i]))  
            
    # Get the lowest 5 uned scores' indices
    pces_indices = np.argsort(-np.array(pces))[:2]
    
    secs = []
    
    for i in range(len(dataset_series)):
        if i != 0:
            secs.append(ssmml.squared_euclidean_cosine(unknown, features[i]))
            
    # Get the lowest 5 uned scores' indices
    secs_indices = np.argsort(np.array(secs))[:2]
    
    sfoecs = []
    
    for i in range(len(dataset_series)):
        if i != 0:
            sfoecs.append(ssmml.squared_first_difference_euclidean_cosine(unknown, features[i]))
            
    # Get the lowest 5 uned scores' indices
    sfoecs_indices = np.argsort(-np.array(sfoecs))[:2]
    
    # Take weighed sum of all criterea
    total = []
    
    for i in range(len(pces)):
        t = (pces[i] + 1) / 2
        t += (secs[i] / math.sqrt(len(unknown[0])))
        t += sfoecs[i]
        t += (uneds[i] / math.sqrt(len(unknown[0])))
        total.append(t)
        
    total_indices = np.argsort(-np.array(total))[:2]
    
    print("Overall best match:")
    
    for i in total_indices:
        print(datasets[i]["title"])
        print(dataset_uuids[i])
    
    print("Pearson Correlation Coefficient:")
    
    for i in pces_indices:
        print(datasets[i]["title"])
        print(dataset_uuids[i])
        
    print("Squared Euclidean Cosine:")
    
    for i in secs_indices:
        print(datasets[i]["title"])
        print(dataset_uuids[i])
        
    print("Squared First Order Euclidean Cosine:")
    
    for i in sfoecs_indices:
        print(datasets[i]["title"])
        print(dataset_uuids[i])    

    print("Unit Normalized Euclidean Distance:")

    # Print the title and URL for each data series selected
    for i in uned_indices:
        print(datasets[i]["title"])
        print(dataset_uuids[i])
        
                
        
    

