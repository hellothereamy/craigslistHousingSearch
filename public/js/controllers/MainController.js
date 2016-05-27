app.controller('myCtrl', function($scope, $http, Solstice) {
	$scope.results = [];
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
	$scope.facetClicked["subarea_s"]=[];
	$scope.countClick = function(e){
		$scope.count ++;
		$http.post('/', { data: new Date().toString()+",click,"+$scope.count }).then(function (success) {
			console.log(success);
		}, function(error) {
			console.log(error);
		});
		console.log($scope.count);
		console.log(e.target);
		if(e.target.tagName == "INPUT"){
			if(e.target.type == "checkbox"){
				if(e.target.checked){
					if(e.target.name === "subarea_s"){
						$scope.facetClicked[e.target.name].push(e.target.id);
					}
					else $scope.facetClicked.push(e.target.name);

					console.log($scope.facetClicked);
					$http.post('/', { data: new Date().toString()+",facet,"+$scope.facetClicked }).then(function (success) {
						console.log(success);
					}, function(error) {
						console.log(error);
					});
				}
				else{
					if (e.target.name =="subarea_s"){
						var index = $scope.facetClicked["subarea_s"].indexOf(e.target.id);
						$scope.facetClicked["subarea_s"].splice(index,1);
					}
					else{
						var index = $scope.facetClicked.indexOf(e.target.name);
						$scope.facetClicked.splice(index,1);
						console.log($scope.facetClicked);
					}
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
		Solstice.search({
		    q: currQuery,
		    rows: 10,
		    fl: '*,score'
		}).then(function (data){
		    $scope.results = data.data.response.docs;
		    $scope.filteredResults = $scope.results.filter(function(result){
		    	for(var i =0; i<$scope.facetClicked.length;i++){
		    		if(result.$scope.facetClicked[i] == "subarea_s"){
		    			for( var j=0; j < $scope.facetClicked[i].length; j++){
		    				if(result.$scope.facetClicked[i] == $scope.facetClicked[j]){
		    					
		    				}
		    			}
		    		}
		    		else (result.$scope.facetClicked[i] == result) {
		    			if(result.$scope.facetClicked[i] == false){
		    				break;
		    			}
		    		}
		    	}
		    });

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

