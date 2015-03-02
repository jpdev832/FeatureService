'use strict';

/* Services */

var services = angular.module('services', ['ngResource']);

services.factory('Place', ['$resource',
  function($resource){
	// TODO add remaining REST endpoints
    //return $resource('/test/data/place');

	 return $resource('http://jpdev832.dyndns.org:8089/place'); //Endpoint
//    return $resource('/test/data/place.json', {id: "id"},{
//    	get: {method: 'GET', isArray:false}
//    });

    
 }]);

services.factory('Feature', ['$resource',
         function($resource){
    // return $resource('/place/features');
	 return $resource('http://jpdev832.dyndns.org:8089/place/feature'); //Endpoint

//	return $resource('/test/data/feature.json', {id: "id"},{
//    	get: {method: 'GET', isArray:false}
//    });        
    
 }]);                  
                         