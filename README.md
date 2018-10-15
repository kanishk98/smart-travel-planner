# Travel Planner for Android
A schedule-aware travel planner app that recommends destinations to users and connects them to relevant booking sources. 

## Roadmap

- [x] Login screen with Google Sign-in
- [x] Integration with Calendar API (ContentProvider or GoogleCalendar, either is acceptable)
- [x] Accept details screen with fields for Age, Gender, Budget, Duration of intended stay
- [ ] Integration with REST API (please wait on @kanishk98 for this)
- [ ] Final result screen with backend-supplied suggestions

## Model explanation

The training file can be found in `predictor_model/Kerasmodel.py`. It is a Keras-based implementation of 3 different neural networks (we couldn't nail down one approach, so we tried all 3) that generates 3 `.h5` files and feeds them into `predictor_model/server.py` for consumption by the Android app. 

We chose a neural network-based approach because of their simplicity as universal function approximators. We considered and disregarded the following classifiers for the following reasons:

1. Decision trees (too many small variations in the dataset, will form different trees)
2. SVM (classes of countries to travel to are very close, for patterns not expressed clearly in the values of dataset features)
3. Utility matrix - based classification: same reason as above

In addition, neural networks, if not doing a bare-bones implementation, are simpler to understand and maintain, and abstract away a lot of our work. While most of deep learning is sorcery, we assumed that the layer-based implementation of deep learning will be enough to extract information from this dataset. Also, our problem is end-to-end: supply information and get most likely travel destinations.

### `Kerasmodel.py`



## Contributing 

Please fork this repository and checkout to a new branch before beginning work. 