'use strict';

var recommendation = angular.module('recommendation', ['ngRoute', 'controllers','services']);

recommendation.config(['$routeProvider', 
   function($routeProvider){

	$routeProvider.
		when('/places', {
			templateUrl: 'partials/search.html',
			controller: 'SearchCtrl'
		}).
		when('/features', {
			templateUrl: 'partials/search.html',
			controller: 'SearchCtrl'
		}).
		when('/places/add', {
			templateUrl: 'partials/places/add.html',
			controller: 'AddCtrl'
		}).
		when('/features/add', {
			templateUrl: 'partials/features/add.html',
			controller: 'AddCtrl'
		}).
		otherwise({
	        redirectTo: '/places'
	      });
}]);