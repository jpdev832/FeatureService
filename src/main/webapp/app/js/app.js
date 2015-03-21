'use strict';

var recommendation = angular.module('recommendation', ['ngRoute', 'controllers','services','directives','anguFixedHeaderTable']);

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
			templateUrl: 'partials/places/submit.html',
			controller: 'SubmitCtrl'
		}).
		when('/features/add', {
			templateUrl: 'partials/features/submit.html',
			controller: 'SubmitCtrl'
		}).
		when('/places/view/:id', {
			templateUrl: 'partials/places/submit.html',
			controller: 'SubmitCtrl'
		}).
		when('/features/view/:id', {
			templateUrl: 'partials/features/submit.html',
			controller: 'SubmitCtrl'
		}).
		otherwise({
	        redirectTo: '/places'
	      });
}]);