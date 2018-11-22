const express = require('express');
const fetch = require('node-fetch');
const app = express();
app.use(express.json());
app.use(function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});
app.get('/predict', async(req, res) => {
	const quarter = req.query.quarter;
	const age = req.query.age;
	const gender = req.query.gender;
	const duration = req.query.duration;
	const budget = req.query.budget;
	const data = await fetch('http://localhost:5000/predict?quarter=' + quarter + '&age=' + age + '&gender=' + gender + '&duration=' + duration + '&budget=' + budget)
	/*.then(suc => {
		console.log(suc);
		res.send(suc.json());
	})
	.catch(err => {
		res.send(err);
	})*/
	res.send(await data.json());
});
app.listen(5001, () => {
	console.log('Server online');
})
