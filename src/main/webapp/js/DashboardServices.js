'use strict'

var dashboardServices = angular.module('DashboardServices', []);

dashboardServices.factory('DashServices', function($resource, SessionService, flash) {

    var getChartRes = $resource('api/dashboard/charts/:userId', {}, {
        getChart: {
            method: 'GET'
        }
    });

    var userId = SessionService.get('userId');

    return {
        getChart: function() {

            var chartData = getChartRes.getChart({
                userId: userId
            }).$promise;

            chartData.then(function(response) {
                return response.data;
            }, function(response) {
                $scope.errorresource = response.data;
                flash.pop({
                    title: '',
                    body: $scope.errorresource.message,
                    type: 'alert-danger'
                });
            });
            return chartData;
        }
    };
});