'use strict';

/* Controllers */

//TODO move each controller to its own file
var controllers = angular.module('controllers', []);

controllers.controller('SubmitCtrl', [ '$scope','$location', '$routeParams', 'Place', 'Feature',
	function($scope, $location, $routeParams, Place, Feature ) {
	
	if($location.path() == '/places/view/'+$routeParams.id){

		$scope.resource = "Place";
		Place.get({id: $routeParams.id}, function(resource){
			$scope.data = resource.data[0];
		});
		
	}else if ($location.path() == '/features/view/'+$routeParams.id){
		$scope.resource = "Feature";
		Feature.get({id: $routeParams.id}, function(resource){
			$scope.data = resource.data[0];
		});
	}
	
	$scope.save = function() {
		
		if($scope.data.features.length > 0){
			
			for(var i=0; i<$scope.data.features.length; i++){	
				var feature = Feature.get({name: $scope.data.features[i].name, category: $scope.data.features[i].category }, function(){});
				if(angular.equals({}, feature)){
					var feature = new Feature($scope.data.features[i])
					feature.$save();
				}
			}
		}
		
		if($scope.resource == "Place"){
			var place = new Place($scope.data);
			place.$save({},
				function(success){
				   $scope.addSuccess = true;
				},
				function(error){
					 $scope.addError = true;
				});
		}else if ($scope.resource == "Feature"){
			var feature = new Feature($scope.data);
			feature.$save({},
					function(success){
				   $scope.addSuccess = true;
				},
				function(error){
					 $scope.addError = true;
				});
		}
	};
	
	$scope.addFeature = function(){
		
		if($scope.data && $scope.data.features){
			$scope.data.features.push({});
		}else{
			$scope.data = {};
			$scope.data.features = [];
			$scope.data.features.push({});
		}
		
	}
	
	$scope.deleteFeature = function(index){
		
		$scope.data.features.splice(index,1)
		
	}
	
}

]);

controllers.controller('SearchCtrl', [ '$scope', '$location', 'Place', 'Feature', 'AutoCompletePlace',
	'AutoCompleteFeature', function($scope, $location, Place, Feature, AutoCompletePlace, AutoCompleteFeature) {

	var resource;
	$scope.defaultField = "Search By";
	$scope.searchText = "";

	if($location.path() == "/places"){
		resource = "places";
		loadPlaceFields();
	}else if ($location.path() == "/features"){
		resource = "features";
		loadFeatureFields();
	}
	getResources({})

	$scope.search = function() {

		if ($scope.searchField) {
			
			var filter = {};
			filter[$scope.searchField] = $scope.searchText;
			getResources(filter)
			
		}
	};

	/**
	* AutoComplete requests
	*/
	$scope.autoComplete = function(autoText){
		console.log("auto text: "+autoText);
		if ($scope.searchField) {
			var filter = { property:$scope.searchField, q:autoText };

			if(resource == "places"){
				AutoCompletePlace.get(filter, function(response){
					if(response.data == null)
						$scope.suggestions = [];
					else
						$scope.suggestions = response.data.sort();
				});
			}else if (resource == "features"){
				AutoCompleteFeature.get(filter, function(response){
					if(response.data == null)
						$scope.suggestions = [];
					else
						$scope.suggestions = response.data.sort();
				});
			}
		}
	};

	/**
	* Set auto search text to completed text
	**/
	$scope.autoCompleteSelected = function(autoText) {
		$scope.searchText = autoText;
		$scope.suggestions = [];
	};

	$scope.orderByProxy = function(x) { return x; }
    
	$scope.add = function(){
		$location.path($location.path()+"/add")		
	}
	
	function getResources(filter){
		
		if(resource == "places"){
			Place.get(filter, function(response){
				$scope.places = response.data;
			});

		}else if (resource == "features"){
			Feature.get(filter, function(response){
				$scope.features = response.data;
			});
		}
	}
	
	$scope.view = function(id){
		$scope.idSelectedItem = id;
		$location.path($location.path()+"/view/"+id)
		
	}
	
	//TODO get from API
	function loadPlaceFields(){
		$scope.fields = [{key:"name", val:'Name'}, {key:"location", val:'Location'}, {key:"neighborhood", val:'Neighborhood'}, {key:"type", val:'Type'}, {key:"city", val:'City'}, {key:"state", val:'State'}, {key:"country", val:'Country'}]
	}
	
	function loadFeatureFields(){
		
		$scope.fields = [{key:"name", val:'Name'}, {key:"category", val:'Category'},{key:"value", val:'Value'}]
	}
	


}]);

controllers.controller('HeaderCtrl', [ '$scope', '$location', function($scope, $location) {
	
    $scope.isActive = function (viewLocation) { 
    	$scope.location = viewLocation;
        return viewLocation === $location.path();
    };
  
}]);

