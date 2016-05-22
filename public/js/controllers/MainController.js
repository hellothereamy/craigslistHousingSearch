app.controller('myCtrl', function($scope, $http) {
	window.onload = function(){
		start = new Date();
		console.log(start.getTime());
		alert(start.getTime());
		$http.post('/', start.getTime()).then(function (success) {
			console.log(success);
		}, function(error) {
			console.log(error);
		});
	};
	$scope.count = 0;
	$scope.countClick = function(){
		$scope.count ++;
		console.log($scope.count);
	}
	$scope.queries = [];

	$scope.handleQuery = function(){
		var currQuery = document.getElementById("query").value;
		console.log(currQuery);
		$scope.queries.push(currQuery);
		console.log($scope.queries);
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

