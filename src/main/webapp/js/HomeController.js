var homeControllers = angular.module('HomeControllers', []);

homeControllers.controller('HomeController',
        function($scope, $log, $location) {
            $scope.imagelists = [{url: "img/background2.jpg?v=1&cache=" + (new Date()).getTime()}, {url: "img/background3.jpg?v=1&cache=" + (new Date()).getTime()}, {url: "img/background5.jpg?v=1&cache=" + (new Date()).getTime()}, {url: "img/background6.jpg?v=1&cache=" + (new Date()).getTime()}];
            $log.info = 'Inside Index Controller';
        });