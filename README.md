# Travel Planner for Android

While people are often eager to travel to new and exciting locations, they face difficulties in deciding a time/place to travel based on their schedule and interests, respectively. A lot of efforts are spent on deciding where and when to go, and such decisions tend to be based on largely subjective opinions of people and/or travel sites. These are helpful sometimes but do not understand the user and his/her work patterns, budget, or interests. 

Our app attempts to solve this problem by means of a shallow neural network served via HTTP and hosted on an AWS ec2 instance. 

## Roadmap

- [x] Login screen with Google Sign-in
- [x] Integration with Calendar API (ContentProvider or GoogleCalendar, either is acceptable)
- [x] Accept details screen with fields for Age, Gender, Budget, Duration of intended stay
- [x] Integration with REST API (please wait on @kanishk98 for this)
- [x] Final result screen with backend-supplied suggestions

## Dependencies/permissions
1. User permissions to access their Android Calendar
2. Working Internet Connection
3. Android 4.0 or higher 

## Audience and required input 
The application does not have a restricted user criterion. It can be used by anybody that needs help deciding on a vacation spot. All the user needs to do is input the following details: 
1. Gender 
2. Budget
3. Annual Quarter (Jan-Mar, Apr-Jun, Jul-Sep, Oct-Dec) 

Note: The app assumes that the user has an active calendar as it will use the calendar to gain useful insights into when and for how long the user is free. 

## Model 
Our dataset is taken directly from Kaggle. It includes travel data from within the United Kingdom to several countries around the world. It includes 18047 data rows with 14 variables(including the target variable).

Keras was used to design a Deep Learning model to output a destination suitable for the user as per his/her specifications with 100 epochs and an accuracy of 9.5-10%. We feel that 10% on an 81 class dataset with just 18k examples is good enough. Since it’s a continuous function, even when it errs, it gives a country similar to the one expected anyway. 

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

## Workflow
The app opens to a login page where you can use your Google account to sign in which is how the app will connect to the user’s Google Calendar when it asks for permission right after logging in. The user has the option to save those credentials.

The next input page is where the user is requested to input the parameters he/she wants to provide and submit the form. The app then sends an API request to the server with the relevant provided information which includes the dates he/she is free after accessing their calendar.

The server uses that information packet to determine the most viable destination with the Keras pre-trained model and sends back the required information back to the app which displays it aptly for the user’s perusal.

Note: The app only provides suggestions and gives advice as to where the user can travel to in their ‘vacation’ period. It does not make any bookings, etc.

## Contributing 

Please fork this repository and checkout to a new branch before beginning work. 
