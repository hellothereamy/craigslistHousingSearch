var express = require('express');
var fs = require("fs");
var app = express();

app.use(express.static("public"));

// app.get('/', function (req, res) {
//   res.sendFile("index.html");
// });

app.listen(3000, function () {
  console.log('Example app listening on port 3000!');
});

//handle log updates
app.post('/', function(req, res) {
	fs.appendFile('message.txt', 'data to append', (err) => {
	  if (err) throw err;
	  console.log('The '+req.body+' was appended to file!');
	});
});