'use strict';

var recommendation = angular.module('recommendation', ['ngRoute', 'controllers']);

recommendation.config(['$routeProvider', 
   function($routeProvider){

	$routeProvider.
		when('/submit', {
			templateUrl: 'partials/submit.html',
			controller: 'SubmitCtrl'
		}).
		otherwise({
	        redirectTo: '/submit'
	      });
}]);