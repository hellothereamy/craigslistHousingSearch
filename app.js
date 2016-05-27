var express = require('express');
var fs = require("fs");
var bodyParser = require('body-parser');
var app = express();

app.use(express.static("public"));
app.use(bodyParser.json({limit: '50mb'}));
app.use(bodyParser.urlencoded({limit: '50mb', extended: true}));

app.listen(3000, function () {
  console.log('Example app listening on port 3000!');
});

//handle log updates
app.post('/', function(req, res) {
	fs.appendFile('log.txt', req.body.data + '\n', (err) => {
	  if (err) throw err;
	  console.log(req.body.data +' was appended to file!\n');
	  res.send(req.body.data);
	});
});

app.post('/search', function(req, res) {
	var obj = JSON.parse(fs.readFileSync('queryHouse.json', 'utf8'));

	res.send(obj);
});