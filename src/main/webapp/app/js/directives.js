'use strict';

/* Directives */

var directives = angular.module('directives', []);

directives.directive("autoComplete", function(){
    return{
        //Use global scope for now
        templateUrl: "partials/autoComplete.html",
        restrict: 'E',
        link: function(scope, element, attrs){}
    };
});