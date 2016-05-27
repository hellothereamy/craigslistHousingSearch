appB.controller('bCtrl', function($scope, $http, Solstice, NgMap, NavigatorGeolocation, GeoCoder) {
	$scope.results = [];
	$scope.latlng = "0,0";
	$scope.googleMapsUrl = "https://maps.googleapis.com/maps/api/js?key=AIzaSyAy2m_H-odemZ5FYX6GXyYXpnacj7s7X4U";
	NgMap.getMap().then(function(map) {
	    console.log(map.getCenter());
	    console.log('markers', map.markers);
	    console.log('shapes', map.shapes);
	});
	NavigatorGeolocation.getCurrentPosition().then(function(position) {
    	$scope.latlng = position.coords.latitude + "," + position.coords.longitude;
    	console.log($scope.latlng);
	});
	window.onload = function(){
		start = new Date();
		console.log(start.getTime());
		alert(start.getTime());
		$http.post('/', { data: new Date().toString()+",start,"+start.getTime() }).then(function (success) {
			console.log(success);
		}, function(error) {
			console.log(error);
		});
	};
	$scope.count = 0;
	$scope.facetClicked =[];
	$scope.countClick = function(e){
		$scope.count ++;
		$http.post('/', { data: new Date().toString()+",click,"+$scope.count }).then(function (success) {
			console.log(success);
		}, function(error) {
			console.log(error);
		});
		console.log($scope.count);
		console.log(e.target.type);
		if(e.target.tagName == "INPUT"){
			if(e.target.type == "checkbox"){
				if(e.target.checked){
					$scope.facetClicked.push(e.target.name);
					console.log($scope.facetClicked);
					$http.post('/', { data: new Date().toString()+",facet,"+$scope.facetClicked }).then(function (success) {
						console.log(success);
					}, function(error) {
						console.log(error);
					});
				}
				else{
					var index = $scope.facetClicked.indexOf(e.target.name);
					$scope.facetClicked.splice(index,1);
					console.log($scope.facetClicked);
					$http.post('/', { data: new Date().toString()+",facet,"+$scope.facetClicked }).then(function (success) {
						console.log(success);
					}, function(error) {
						console.log(error);
					});
				}
			}
		}
	}
	$scope.queries = [];
	$scope.queryCount=0;

	$scope.handleQuery = function(){
		var currQuery = document.getElementById("query").value;
		console.log(currQuery);
		// Solstice.search({
		//     q: currQuery,
		//     rows: 10,
		//     fl: '*,score'
		$http.post('/search').then(function (data){
		    $scope.results = data.data.response.docs;
		    $scope.filteredResults = $scope.results.filter(function(result){
		    	for(var i = 0; i<$scope.facetClicked.length; i++){
		    		if(result.$scope.facetClicked[i]) {
		    			
		    		}
		    	}
		    });
		    $scope.coords = [];
		    $scope.results.forEach(function(result) {
		    	GeoCoder.geocode({address:result.street_address_s + ","+result.subarea_s}).then(function(data) {
		    		$scope.coords.push(data[0].geometry.location.lat() + "," + data[0].geometry.location.lng());
		    	});
		    });
		    console.log($scope.coords);
		    console.log(data.data.response.docs);
		});
		


		$scope.queries.push(currQuery);
		$http.post('/', { data: new Date().toString()+",query,"+currQuery }).then(function (success) {
			console.log(success);
		}, function(error) {
			console.log(error);
		});
		console.log($scope.queries);
		$scope.queryCount ++;
		$http.post('/', { data: new Date().toString()+",queryCount,"+$scope.queryCount }).then(function (success) {
			console.log(success);
		}, function(error) {
			console.log(error);
		});
		console.log("query count: "+$scope.queryCount);
	};
	window.onbeforeunload= function(event){
		end = new Date();
		totalTime = ( end.getTime()-start.getTime() );
		totalTime = (totalTime/1000).toFixed(2);
		console.log(totalTime + " secs");
		$http.post('/', { data: new Date().toString()+",stop,"+totalTime }).then(function (success) {
			console.log(success);
		}, function(error) {
			console.log(error);
		});
		start =0;
		$scope.count = 0;
		return(totalTime);
	};
});

