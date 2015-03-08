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
			templateUrl: 'partials/add.html',
			controller: 'AddCtrl'
		}).
		when('/features/add', {
			templateUrl: 'partials/add.html',
			controller: 'AddCtrl'
		}).
		when('/places/update/:id', {
			templateUrl: 'partials/update.html',
			controller: 'UpdateCtrl'
		}).
		when('/features/update/:id', {
			templateUrl: 'partials/update.html',
			controller: 'UpdateCtrl'
		}).
		otherwise({
	        redirectTo: '/places'
	      });
}]);