app.controller('myCtrl', function($scope, $http) {
	window.onload = function(){
		start = new Date();
		console.log(start.getTime());
		alert(start.getTime());
		$http.post('/', new Date().toString()+",start,"+start.getTime()).then(function (success) {
			console.log(success);
		}, function(error) {
			console.log(error);
		});
	};
	$scope.count = 0;
	$scope.facetClicked =[];
	$scope.countClick = function(e){
		$scope.count ++;
		$http.post('/', new Date().toString()+",click,"+$scope.count).then(function (success) {
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
					$http.post('/', new Date().toString()+",facet,"+$scope.facetClicked).then(function (success) {
						console.log(success);
					}, function(error) {
						console.log(error);
					});
				}
				else{
					var index = $scope.facetClicked.indexOf(e.target.name);
					$scope.facetClicked.splice(index,1);
					console.log($scope.facetClicked);
					$http.post('/', new Date().toString()+",facet,"+$scope.facetClicked).then(function (success) {
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
		$scope.queries.push(currQuery);
		$http.post('/', new Date().toString()+",query,"+currQuery).then(function (success) {
			console.log(success);
		}, function(error) {
			console.log(error);
		});
		console.log($scope.queries);
		$scope.queryCount ++;
		$http.post('/', new Date().toString()+",queryCount,"+$scope.queryCount).then(function (success) {
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
		start =0;
		$scope.count = 0;
		return(totalTime);
	};


	$scope.results = [
		{
			link: 'http://images.craigslist.org/00S0S_PPirCqKK4f_600x450.jpg',
			title: 'Lovely 1 bedroom apartment great location mature landscapping',
			price: '$2050',
			br: '1',
			ba: '1',
			sqFt: '1356',
			city: 'cupertino',
			avail: 'available jun 01'
		}
	];
});

