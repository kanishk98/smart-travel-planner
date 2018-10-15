# Travel Planner for Android
A schedule-aware travel planner app that recommends destinations to users and connects them to relevant booking sources. 

## Roadmap

- [x] Login screen with Google Sign-in
- [x] Integration with Calendar API (ContentProvider or GoogleCalendar, either is acceptable)
- [x] Accept details screen with fields for Age, Gender, Budget, Duration of intended stay
- [ ] Integration with REST API (please wait on @kanishk98 for this)
- [ ] Final result screen with backend-supplied suggestions

## Model explanation

The training file can be found in `predictor_model/Kerasmodel.py`. It is a Keras-based implementation of 3 different neural networks (reason for this explained below) that generates 3 `.h5` files and feeds them into `predictor_model/server.py` for consumption by the Android app. 

We chose a neural network-based approach because of their simplicity as universal function approximators. We considered and disregarded the following classifiers for the following reasons:

1. Decision trees (too many small variations in the dataset, will form different trees)
2. SVM (classes of countries to travel to are very close, high-dimensional dataset)
3. Utility matrix - based classification: same reason as above

In addition, neural networks, if not doing a bare-bones implementation, are simpler to understand and maintain, and abstract away a lot of our work. While most of deep learning is sorcery, we assumed that the layer-based implementation of deep learning will be enough to extract information from this dataset. Also, our problem is end-to-end: supply information and get most likely travel destinations.

### `Kerasmodel.py`

Since this is a shallow neural network, it might overfit the training dataset. To avoid that, we use three different models and take the average of their predictions in `server.py`. The models' summary is:
1. 5-layer neural network, tanh activation function applied throughout, Adam optimiser function
2. 4-layer neural network, relu and softmax (for the output layer) activation function, SGD optimiser function
3. 5-layer neural network, relu and softmax (for the output layer) activation function, Adagrad optimiser function

Categorical crossentropy was the loss function we tried to minimise. 
In each of the 3 cases, we pass the dataset through the network 100 times (epochs), in 64-sized batches. 

```
for i in range(tsize):
	x_train[i][0] =  df['quarter'][i]
	x_train[i][1] = df['Age'][i]
	if df['Sex'][i] == '#NULL!':
		x_train[i][2] = 0.5	
	else: 
		x_train[i][2] = df['Sex'][i]
	x_train[i][3] = df['duration'][i]
	if df['spend'][i] <= '50000':
		x_train[i][4] = 1
	elif df['spend'][i] <= '200000':
		x_train[i][4] = 2
	elif df['spend'][i] <= '500000':
		x_train[i][4] = 3
	elif df['spend'][i] <= '2000000':
		x_train[i][4] = 4
	elif df['spend'][i] <= '5000000':
		x_train[i][4] = 5
	else:
		x_train[i][4] = 6
	y_train[i] = df['country'][i] - 9
y_train = to_categorical(y_train, num_classes = 85)
```

The above snippet explains how we're assigning numbers to classes of feature values.

### `server.py`

Standard Flask REST API for consuming the predictions via HTTP requests coming from the Android app. 

Accepts HTTP query parameters, fits them to the type expected by the trained model, and sends back the averaged results. Here's where all of the important stuff happens:

```
    categorize(x_in)

    pred1 = loaded_model1.predict(x_in)
    pred2 = loaded_model2.predict(x_in)
    pred3 = loaded_model3.predict(x_in)

    pred = (pred1 + pred2 + pred3) / 3
```

The rest of the work done here is mostly HTTP-related and deals with formatting the output. 

## Contributing 

Please fork this repository and checkout to a new branch before beginning work. 