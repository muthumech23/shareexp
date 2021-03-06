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


shareExpDirectives.directive('tooltip', function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            $(element)
                .attr('title', scope.$eval(attrs.tooltip))
                .tooltip({
                    placement: "bottom"
                });
        }
    }
});

shareExpDirectives.directive('datepickerse', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attrs, ngModelCtrl) {
            $(function() {
                element.datepicker({
                    dateFormat: 'MM dd, yy',
                    onSelect: function(date) {
                        ngModelCtrl.$setViewValue(date);
                        scope.$apply();
                    }
                });
            });
        }
    }
});

shareExpDirectives.directive('disableAnimation', function($animate){
    return {
        restrict: 'A',
        link: function($scope, $element, $attrs){
            $attrs.$observe('disableAnimation', function(value){
                $animate.enabled(!value, $element);
            });
        }
    }
});