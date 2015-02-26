'use strict'
var dashboardControllers = angular.module('DashboardControllers', []);

dashboardControllers.controller('DashController', function($scope, $log, $location, homeBillData, getChartData, $filter, SessionService) {

	$scope.status = {
        isFirstOpen: true,
        isFirstDisabled: false
    };

    $scope.OwesLoggedUser = [];

    $scope.loggedUserOwe = [];

	angular.forEach(homeBillData, function(userInfo) {

		var userId = SessionService.get('userId');
        var userName = userInfo.name;
		if(userId != userInfo.userId){
			angular.forEach(userInfo.amtCurrs, function(userAmts) {
                if (userAmts.amountStatus === 'D') {
            	    var userAmt = userAmts.currency + $filter('number')(userAmts.amount, '2');
                    $scope.OwesLoggedUser.push({name: userName, amount: userAmt});
                }else if(userAmts.amountStatus === 'C'){
            	    var userAmt = userAmts.currency + $filter('number')(userAmts.amount, '2');
                    $scope.loggedUserOwe.push({name: userName, amount: userAmt});
                }
            });
		}

	});

	$scope.config = {
        title: '',
        tooltips: true,
        labels: false,
        mouseover: function() {},
        mouseout: function() {},
        click: function() {},
        legend: {
          display: true,
          //could be 'left, right'
          position: 'right'
        },
        innerRadius: 0,
        lineLegend: "traditional",
        waitForHeightAndWidth: false
      };

    $scope.yearData = {
        series: ['Income', 'Expense', 'Saving'],
        data: getChartData.chartYear
    };

	$scope.budgetData = {
        series: ['Budget', 'Expense'],
        data: getChartData.chartBudget
      };

    $scope.chartYear = {
        "type": "ColumnChart",
        "data": getChartData.chartYearSummary,
        "options": {
            "title": "",
            "isStacked": "false",
            "fill": 20,
            "displayExactValues": true,
            "vAxis": {
                "title": "",
                "gridlines": {
                    "count": 10
                }
            },
            "hAxis": {
                "title": ""
            }
        },
        "formatters": {},
        "displayed": true
    }

    $scope.chartBudget = {
        "type": "LineChart",
        "data": getChartData.chartBudgetSummary,
        "options": {
            "title": "",
            "isStacked": "false",
            "fill": 20,
            "displayExactValues": true,
            "vAxis": {
                "title": "",
                "gridlines": {
                    "count": 10
                }
            },
            "hAxis": {
                "title": ""
            }
        },
        "formatters": {},
        "displayed": true
    }

});