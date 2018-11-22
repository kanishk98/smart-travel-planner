const express = require('express');
const fetch = require('node-fetch');
const app = express();
app.use(express.json());
app.use(function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});
app.get('/predict', (req, res) => {
	const quarter = req.query.quarter;
	const age = req.query.age;
	const gender = req.query.gender;
	const duration = req.query.duration;
	const budget = req.query.budget;
	fetch('http://localhost:5000/predict?quarter=' + quarter + '&age=' + age + '&gender=' + gender + '&duration=' + duration + '&budget=' + budget)
	.then(res => {
		console.log(res);
	})
	.catch(err => {
		console.log(err);
	})
});
app.listen(3000, () => {
	console.log('Server online');
})
