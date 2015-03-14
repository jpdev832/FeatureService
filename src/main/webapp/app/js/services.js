'use strict';

/* Services */

var services = angular.module('services', ['ngResource']);

var basePath = 'http://jpdev832.dyndns.org:8089'
	
services.factory('Place', ['$resource',
  function($resource){

	
    return $resource(basePath+'/place/:id', {id:'@id'});
    
 }]);

services.factory('Feature', ['$resource',
         function($resource){

	return $resource(basePath+'/place/feature/:id', {id:'@id'});        
    
 }]);                  
                         