//
var app = angular.module('metaNav', []);

app.config(['$sceProvider', function($sceProvider) {
	console.log("configured");
    $sceProvider.enabled(false);
}]);

app.directive('fallbackSrc', function () {
  var fallbackSrc = {
    link: function postLink(scope, iElement, iAttrs) {
      iElement.bind('error', function() {
        angular.element(this).attr("src", iAttrs.fallbackSrc);
      });
    }
   }
   return fallbackSrc;
});

app.controller('MainController',['$scope','$http', function($scope,$http) {
	
	console.log("entered MainController;");
	$scope.title = "Light MetaNavigator";
	
	$scope.inputSite = "";
	$scope.website=""; // docsrc of iframe
	
	$scope.angularEquals = angular.equals; //function
	
	$scope.loadNewPage = function() {
		console.log("loadNewPage function");
		
		var inputSite = $scope.inputSite;
		
		$scope.inputSite = inputSite;
		
		$scope.urlToFile(inputSite);
		
		console.log($scope.website);
		
	}
	
	$scope.urlToFile = function(url){
		console.log("urlToFile function");
	
		$http({
		    method : "POST",
		    url : "http://localhost:8008/server/ws",
		    data : url,
		    headers : {
		        'Content-Type' : 'text/plain'
		    }
        }).success(function (data, status, headers, config) {
        	console.log("success");
        	if(angular.equals(data, "httperror")) {
    			$scope.search();
    		} else {
    			$scope.website = data; //html source retornado c/ sucesso
    		}
        }).error(function (data, status, headers, config) {
        	console.log("failure");
        	$scope.website = "<h1><i>HTML service can't be reached, try later</i></h1>";
        });
	 
	}
	
	$scope.search = function() {
		console.log("search function");
		//http://www.bing.com/search?q=lalala+e+la
		
		var inputSearch = $scope.inputSite;
		var inputSearch = inputSearch.replace(" ","+");
		var inputSearch = "http://www.bing.com/search?q="+inputSearch;
		
		$scope.inputSite = inputSearch;
		
		$scope.urlToFile(inputSearch);
		
	}
	

	
}]);

