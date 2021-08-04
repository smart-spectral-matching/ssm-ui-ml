import json
import os
import sys
import urllib

from ssm_ml import ssmml

if sys.argv[1] == "train":
    
    print("ARGV 3 ")
    print(sys.argv[3])
    #Get the list of all models
    dataset_uuids = sys.argv[2].split(',')
    #print("Uuids:")
    #print(json.loads(urllib.request.urlopen(os.environ["FUSEKI_DATABASE_URL"] + "/api/datasets/uuids").read().decode())[-1])
    global_uuid = json.loads(urllib.request.urlopen(os.environ["FUSEKI_DATABASE_URL"] + "/api/datasets/uuids").read().decode())[-1]
    #uuid_url = urllib.request.urlopen("http://128.219.187.5/api/datasets/" + global_uuid + "/models/uuids")
    #dataset_uuids_json = uuid_url.read().decode()
    #dataset_uuids = json.loads(dataset_uuids_json)
    
    #All the datasets from the database
    datasets = []
    
    #Get each dataset and label axes appropriately
    for uuid in dataset_uuids:
        dataset_url = urllib.request.urlopen(os.environ["FUSEKI_DATABASE_URL"] + "/api/datasets/" + global_uuid + "/models/" + uuid)
        dataset = json.loads(dataset_url.read().decode())
        
        for prop in dataset['model']['@graph']:
            if 'number:Array' in prop.keys():
                
                if "dataseries/1" in prop['@id']:
                    prop['axis'] = 'x-axis'
                else:
                    prop['axis'] = 'y-axis'
                
                axis = []
                for point in prop['number:Array']['@list']:
                    axis.append(float(point['@value']))
                prop['number:Array']['@list'] = axis
        
        datasets.append(dataset)
        
    
    
    filter = ssmml.construct_filters(json.loads(sys.argv[3]))[0]
        
    #Get the labels for each dataset
    labels = filter.getLabels(datasets)
    
    #Get the Features for each dataset, normalized in the range 0-1
    features = filter.getFeatures(datasets)
    features = ssmml.normalize_features(features)
    
    classifier = ssmml.train(labels, features, sys.argv[4])
    
    ssmml.save_model(classifier, sys.argv[5], dataset_uuids, os.environ["ML_DATABASE_HOST"], os.environ["ML_DATABASE_PORT"], 
                     os.environ["ML_DATABASE_NAME"], os.environ["ML_DATABASE_USER"], os.environ["ML_DATABASE_PASSWORD"])
    
    print("Model " + sys.argv[5] + " trained and saved to database.")
    
elif sys.argv[1] == 'predict':
    
    filter_json = ssmml.load_filter(sys.argv[2], os.environ["ML_DATABASE_HOST"], os.environ["ML_DATABASE_PORT"], 
                     os.environ["ML_DATABASE_NAME"], os.environ["ML_DATABASE_USER"], os.environ["ML_DATABASE_PASSWORD"])
    
    filter = ssmml.construct_filters(filter_json)
    
    features = filter.getFeatures(sys.argv[3])
    
    extrema = ssmml.load_extrema(sys.argv[2], os.environ["ML_DATABASE_HOST"], os.environ["ML_DATABASE_PORT"], 
                     os.environ["ML_DATABASE_NAME"], os.environ["ML_DATABASE_USER"], os.environ["ML_DATABASE_PASSWORD"])
    
    for i in range(len(features)):
        
        features[i] = (features[i] - extrema[i * 2]) / (extrema[i * 2 + 1] - extrema[i * 2])
        
    classifier = ssmml.load_model(sys.argv[2], os.environ["ML_DATABASE_HOST"], os.environ["ML_DATABASE_PORT"], 
                     os.environ["ML_DATABASE_NAME"], os.environ["ML_DATABASE_USER"], os.environ["ML_DATABASE_PASSWORD"])
    
    labels = ssmml.load_labels(sys.argv[2], os.environ["ML_DATABASE_HOST"], os.environ["ML_DATABASE_PORT"], 
                     os.environ["ML_DATABASE_NAME"], os.environ["ML_DATABASE_USER"], os.environ["ML_DATABASE_PASSWORD"])
    
    #Get the probability function over the grid
    if hasattr(classifier, "decision_function"):
        prob = classifier.decision_function([features])
    else:
        prob = classifier.predict_proba([features])[:, 1]
    
    print("Sample is of class '" + str(labels[0]) + "' with confidence " +str(prob))