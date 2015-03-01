'use strict';

/* Controllers */

var controllers = angular.module('controllers', []);

controllers.controller('AddCtrl', [ '$scope','$location', 'Place', 'Feature', function($scope, $location, Place, Feature) {

	var resource;
	
	
	
	$scope.save = function() {
		if($location.path() == "/places/add"){
			
			var place = toPlace($scope.data)
			place.$save();
			
		}else if ($location.path() == "/features/add"){
			
			var feature = toFeature($scope.data)
			Feature.$save();
		}
	};
	
	function toPlace(data){
		var place = new Place();
		place.name = data.name;
		place.location = data.location;
		place.neighborhood = data.neighborhood;
		place.city = data.city;
		place.state = data.state;
		place.country = data.country;
		return place;
	}
	
	function toFeature(data){
		var feature = new Feature();
		feature.name = data.name;
		feature.category = data.category;
		feature.value = data.value;
		
		return feature;
	}

}

]);

controllers.controller('SearchCtrl', [ '$scope', '$location', 'Place', 'Feature', function($scope, $location, Place, Feature) {
	// TODO add service to handle REST calls
	

	var resource;
	
	$scope.defaultField = "Search By";
	if($location.path() == "/places"){
		resource = "places";
		loadPlaceFields();
	}else if ($location.path() == "/features"){
		resource = "features";
		loadFeatureFields();
	}
	

	$scope.search = function() {
		

		if ($scope.selectedField && $scope.searchText) {
			if(resource == "places"){
				
				Place.get(function(data){
					$scope.data = data;

				});
				
			}else if (resource == "features"){
				Feature.get(function(data){
					$scope.data = data;
				});
			}
		}
	};
	
	
	function loadPlaceFields(){
		$scope.fields = [{val:'Name'}, {val:'Location'}, {val:'Neighborhood'}, {val:'City'}, {val:'State'}, {val:'Country'}]
	}
	
	function loadFeatureFields(){
		
		$scope.fields = [{val:'Name'}, {val:'Category'},{val:'Value'}]
	}
	


}]);

controllers.controller('HeaderCtrl', [ '$scope', '$location', function($scope, $location) {

    $scope.isActive = function (viewLocation) { 
    	$scope.location = viewLocation;
        return viewLocation === $location.path();
    };
    
	$scope.add = function(){
		$location.path($location.path()+"/add")
		
	}
    
  
}]);

