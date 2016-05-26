var app = angular.module("myApp", ['solstice']);

app.config(function(SolsticeProvider) {
	var ip = "172.21.114.33";
	console.log(ip);
  	SolsticeProvider.setEndpoint('http://'+ ip + ':8983/solr/searchEngineCollection');
});