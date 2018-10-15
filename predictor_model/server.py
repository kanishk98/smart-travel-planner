import numpy as np
from keras.models import model_from_json
import flask
from flask import jsonify
import os
import json
from flask import Flask

app = Flask(__name__)

def load_model():
    global loaded_model1, loaded_model2, loaded_model3
    json_file = open('model1.json', 'r')
    loaded_model_json = json_file.read()
    json_file.close()
    loaded_model1 = model_from_json(loaded_model_json)
    # load weights into new model
    loaded_model1.load_weights("model1.h5")

    json_file = open('model2.json', 'r')
    loaded_model_json = json_file.read()
    json_file.close()
    loaded_model2 = model_from_json(loaded_model_json)
    # load weights into new model
    loaded_model2.load_weights("model2.h5")

    json_file = open('model3.json', 'r')
    loaded_model_json = json_file.read()
    json_file.close()
    loaded_model3 = model_from_json(loaded_model_json)
    # load weights into new model
    loaded_model3.load_weights("model3.h5")


def categorize(data):
    if data[0][1] < 15:
        data[0][1] = 1
    elif data[0][1] < 25:
        data[0][1] = 2
    elif data[0][1] < 35:
        data[0][1] = 3
    elif data[0][1] < 45:
        data[0][1] = 4
    elif data[0][1] < 55:
        data[0][1] = 5
    elif data[0][1] < 65:
        data[0][1] = 6
    else:
        data[0][1] = 7

    if data[0][3] < 2:
        data[0][3] = 1
    elif data[0][3] < 3:
        data[0][3] = 2
    elif data[0][3] < 6:
        data[0][3] = 3
    elif data[0][3] < 10:
        data[0][3] = 4
    elif data[0][3] < 15:
        data[0][3] = 5
    else:
        data[0][3] = 6

    if data[0][4] < 50000:
        data[0][4] = 1
    elif data[0][4] < 200000:
        data[0][4] = 2
    elif data[0][4] < 500000:
        data[0][4] = 3
    elif data[0][4] < 2000000:
        data[0][4] = 4
    elif data[0][4] < 5000000:
        data[0][4] = 5
    else:
        data[0][4] = 6

@app.route("/predict", methods=["GET"])
def json_message():
    x_in = np.random.randn(1, 5)

    x_in[0][0] = float(flask.request.args.get('quarter'))
    x_in[0][1] = float(flask.request.args.get('age'))
    if flask.request.args.get('sex') == 'female':
        x_in[0][2] = 1
    else:
        x_in[0][2] = 0
    
    x_in[0][3] = float (flask.request.args.get('duration'))
    x_in[0][4] = float (flask.request.args.get('budget'))

    categorize(x_in)

    pred1 = loaded_model1.predict(x_in)
    pred2 = loaded_model2.predict(x_in)
    pred3 = loaded_model3.predict(x_in)

    pred = (pred1 + pred2 + pred3) / 3

    i = iter(pred)
    pred_dict = {pred[0][i]: i for i in range(0, 85)}

    pred_dict = sorted(pred_dict.items())

    prefs = []

    for _, value in pred_dict:
        prefs.append(value)

    prefs.reverse()
    data = {}

    with open('CountryDB.txt') as f:
        cnt = 1
        line = f.readline()
        while line:
            data[cnt] = line.strip()
            line = f.readline()
            cnt += 1
    ans = []
    for i in range(5):
        ans.append(data[prefs[i]])
    np.random.shuffle(ans)
    ans = ans[0:3]
    return flask.jsonify(ans)


load_model()
app.run()