app.controller('myCtrl', function($scope, $http, Solstice) {
	$scope.results = [];
	$scope.filteredResults = [];
	Solstice.search({
	 q: "*",
	 rows: 50,
	 fl: '*,score'
	}).then(function (data){
		console.log(data);
	    $scope.results = data.data.response.docs;
	});

	$scope.facetFilter = function() {
	    console.log($scope.filteredResults);
		if ($scope.facetClicked["subarea_s"].length <= 0 && $scope.facetClicked["housetype_s"].length <= 0 && Object.keys($scope.facetClicked).length <= 2){
			$scope.filteredResults = $scope.results;
		} else {
		    $scope.filteredResults = $scope.results.filter(function(result){
		    	var flag = true;
		    	console.log(Object.keys($scope.facetClicked));
		    	for(var i =0; i<Object.keys($scope.facetClicked).length;i++){

		    		console.log($scope.facetClicked["housetype_s"]);
		    		console.log(result[$scope.facetClicked[i]]);
		    		console.log("facet is = " + $scope.facetClicked[i]);

		    		if(Object.keys($scope.facetClicked)[i] == "subarea_s" || Object.keys($scope.facetClicked)[i] == "housetype_s"){
			    		if($scope.facetClicked[Object.keys($scope.facetClicked)[i]].length > 0) {
			    			flag = false;
			    			for( var j=0; j < $scope.facetClicked[Object.keys($scope.facetClicked)[i]].length; j++){
			    				console.log($scope.facetClicked[Object.keys($scope.facetClicked)[i]][j]);
			    				if(result[Object.keys($scope.facetClicked)[i]] == $scope.facetClicked[Object.keys($scope.facetClicked)[i]][j]){
			    					flag = true;
			    					break;
			    				}
			    			}
			    			if(!flag) {
			    				break;
			    			}
			    		}
		    		}

		    		else if (!result[$scope.facetClicked[i]]) {
		    			flag=false;
		    			break;
		    		} else {
		    			flag = true;
		    		}
		    	}
		     	return flag;
		   });
	  }
	};

	window.onload = function(){
		start = new Date();
		console.log(start.getTime());
		$http.post('/', { data: new Date().toString()+",start,"+start.getTime() }).then(function (success) {
			console.log(success);
		}, function(error) {
			console.log(error);
		});
	};

	$scope.count = 0;
	$scope.facetClicked =[];
	$scope.facetClicked["subarea_s"]=[];
	$scope.facetClicked["housetype_s"] = [];
	$scope.countClick = function(e){
		$scope.count ++;
		$http.post('/', { data: new Date().toString()+",click,"+$scope.count }).then(function (success) {
			console.log(success);
		}, function(error) {
			console.log(error);
		});
		
		if(e.target.tagName == "INPUT"){
			if(e.target.type == "checkbox"){
				if(e.target.checked){
					if(e.target.name === "subarea_s"){
						$scope.facetClicked[e.target.name].push(e.target.id);
					}
					else if (e.target.name === "housetype_s"){
						$scope.facetClicked[e.target.name].push(e.target.id);
					}
					else $scope.facetClicked.push(e.target.name);
					$scope.facetFilter();
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
					else if (e.target.name =="housetype_s"){
						var index = $scope.facetClicked["housetype_s"].indexOf(e.target.id);
						$scope.facetClicked["housetype_s"].splice(index,1);
					}
					else{
						var index = $scope.facetClicked.indexOf(e.target.name);
						$scope.facetClicked.splice(index,1);
						console.log($scope.facetClicked);
					}
					$scope.facetFilter();
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
		    $scope.facetFilter();
		    console.log($scope.filteredResults);
		    // console.log(data.data.response.docs);
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
	};
});

