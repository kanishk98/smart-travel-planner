import numpy as np
import pandas as pd
import sklearn
from keras.models import Model, Input, Sequential, model_from_json
from keras.layers import Dense, Activation, Average, Dropout
from keras.utils import to_categorical
import keras.backend as K
from keras.losses import categorical_crossentropy
from keras.callbacks import ModelCheckpoint, TensorBoard
from keras.optimizers import Adam, SGD
from sklearn.model_selection import train_test_split
from matplotlib import pyplot as plt

tsize = 18047 # number of rows in the dataset
numepochs = 100

x_train = np.random.randn(tsize, 5)
y_train = np.random.randint(85, size=(tsize, 1))

df = pd.read_csv("TravelDatabase.csv")

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


#Model 1 : 5 layer NN, tanh activations, Adam optimizer

'''x_test = np.random.randn(100, 20)
y_test = to_categorical(np.random.randint(80, size=(100, 1)), num_classes=80)'''

model1 = Sequential()
model1.add(Dense(16, activation='tanh', input_dim=5))
model1.add(Dropout(0.2))
model1.add(Dense(24, activation='tanh'))
model1.add(Dropout(0.4))
model1.add(Dense(32, activation='tanh'))
model1.add(Dropout(0.3))
model1.add(Dense(24, activation='tanh'))
model1.add(Dropout(0.4))
model1.add(Dense(85, activation='softmax'))

model1.compile(loss='categorical_crossentropy',
              optimizer='Adam',
              metrics=['accuracy'])

history1 = model1.fit(x_train, y_train,
          epochs = numepochs,
          batch_size = 64)

#Model 2 : 4 layer NN, relu activation, dropout

model2 = Sequential()
model2.add(Dense(24, activation='relu', input_dim=5))
model2.add(Dropout(0.3))
model2.add(Dense(32, activation='relu'))
model2.add(Dropout(0.2))
model2.add(Dense(64, activation='relu'))
model2.add(Dropout(0.3))
model2.add(Dense(32, activation='relu'))
model2.add(Dropout(0.2))
model2.add(Dense(24, activation = 'relu'))
model2.add(Dropout(0.3))
model2.add(Dense(85, activation='softmax'))

model2.compile(loss='categorical_crossentropy',
              optimizer='SGD',
              metrics=['accuracy'])

history2 = model2.fit(x_train, y_train,
          epochs = numepochs,
          batch_size = 64)

#Model 3 : 5 layered NN, Adagrad optimizer, relu

model3 = Sequential()
model3.add(Dense(32, activation='relu', input_dim=5))
model3.add(Dropout(0.3))
model3.add(Dense(24, activation='relu'))
model3.add(Dropout(0.4))
model3.add(Dense(32, activation='relu'))
model3.add(Dropout(0.4))
model3.add(Dense(24, activation='relu'))
model3.add(Dropout(0.3))
model3.add(Dense(32, activation = 'relu'))
model3.add(Dropout(0.3))
model3.add(Dense(85, activation='softmax'))

model3.compile(loss='categorical_crossentropy',
              optimizer='Adagrad',
              metrics=['accuracy'])



history3 = model3.fit(x_train, y_train,
          epochs = numepochs,
          batch_size = 64)

model1_json = model1.to_json()
with open("model1.json", "w") as json_file:
    json_file.write(model1_json)

# serialize weights to HDF5
model1.save_weights("model1.h5")

model2_json = model2.to_json()

with open("model2.json", "w") as json_file:
    json_file.write(model2_json)

model2.save_weights("model2.h5")

model3_json = model3.to_json()

with open("model3.json", "w") as json_file:
    json_file.write(model3_json)

model3.save_weights("model3.h5")

plt.plot(history1['loss'])
plt.plot(history1['val_loss'])
plt.title('Model loss')
plt.ylabel('loss')
plt.xlabel('epoch')
plt.legend(['train', 'test'], loc='upper right')
plt.show()

plt.plot(history2['loss'])
plt.plot(history2['val_loss'])
plt.title('Model loss')
plt.ylabel('loss')
plt.xlabel('epoch')
plt.legend(['train', 'test'], loc='upper right')
plt.show()

plt.plot(history3['loss'])
plt.plot(history3['val_loss'])
plt.title('Model loss')
plt.ylabel('loss')
plt.xlabel('epoch')
plt.legend(['train', 'test'], loc='upper right')
plt.show()




