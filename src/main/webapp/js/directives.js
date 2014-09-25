'use strict'


var shareExpDirectives = angular.module('ShareExpDirectives', []);

shareExpDirectives.directive('animate', function() {
    return function(scope, elm, attrs) {
        setTimeout(function() {
            elm.addClass('show');
        });
    };
});
shareExpDirectives.directive('remove', function() {
    return function(scope, elm, attrs) {
        elm.bind('click', function(e) {
            e.preventDefault();
            elm.removeClass('show');
            setTimeout(function() {
                scope.$apply(function() {
                    scope.$eval(attrs.remove);
                });
            }, 200);
        });
    };
});
