'use strict'

var shareExpFilters = angular.module('ShareExpFilters', []);

shareExpFilters.filter('offset', function() {
    return function(input, start) {
        start = parseInt(start, 10);
        return input.slice(start);
    };
});