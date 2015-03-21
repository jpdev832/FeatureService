'use strict';

/* Services */

var services = angular.module('services', ['ngResource']);

var basePath = 'http://jpdev832.dyndns.org:8089'
	
services.factory('Place', ['$resource',
    function($resource){
        return $resource(basePath+'/place/:id', {id:'@id'});
    }
]);

services.factory('Feature', ['$resource',
    function($resource){
	    return $resource(basePath+'/place/feature/:id', {id:'@id'});
    }
]);

/**
* AutoComplete service for places
*
* GET: {property:'@property',q:'@query'}
**/
services.factory('AutoCompletePlace', ['$resource',
    function($resource){
        return $resource(basePath+'/autocomplete/place/:property')
    }
]);

/**
* AutoComplete service for features
*
* GET: {property:'@property',q:'@query'}
**/
services.factory('AutoCompleteFeature', ['$resource',
    function($resource){
        return $resource(basePath+'/autocomplete/feature/:property')
    }
]);
                         