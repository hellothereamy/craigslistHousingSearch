var express = require('express');
var fs = require("fs");
var bodyParser = require('body-parser');
var app = express();

app.use(express.static("public"));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.listen(3000, function () {
  console.log('Example app listening on port 3000!');
});

//handle log updates
app.post('/', function(req, res) {
	fs.appendFile('message.txt', req.body['message'], (err) => {
	  if (err) throw err;
	  console.log('The '+ req.body['message'] +' was appended to file!\n');
	});
});