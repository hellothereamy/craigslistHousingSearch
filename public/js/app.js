var app = angular.module("myApp", ['solstice']);

app.config(function(SolsticeProvider) {
	var ip = "52.40.153.249";
	console.log(ip);
  	SolsticeProvider.setEndpoint('http://'+ ip + ':8983/solr/searchEngineCollection');
});