var appB = angular.module("myBApp", ['solstice', 'ngMap']);

appB.config(function(SolsticeProvider) {
	var ip = "67.180.227.211";
	console.log(ip);
  	SolsticeProvider.setEndpoint('http://'+ ip + ':8983/solr/searchEngineCollection');
});