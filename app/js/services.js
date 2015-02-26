'use strict';

/* Services */

var services = angular.module('services', ['ngResource']);

services.factory('FeatureService', ['$resource',
  function($resource){
    return $resource('place/');
 }]);