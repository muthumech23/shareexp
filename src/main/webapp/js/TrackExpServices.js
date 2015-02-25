'use strict'

var trackExpServices = angular.module('TrackExpServices', []);

trackExpServices.factory('ExpenseServices', function($resource, SessionService, flash) {

    var getYearSummaryRes = $resource('api/trackexp/getyearsummary/:userId', {}, {
        getYearSummary: {
            method: 'GET',
            isArray: true
        }
    });
    var userId = SessionService.get('userId');
    return {
        getYearSummary: function() {

            var yearSummaryData = getYearSummaryRes.getYearSummary({
                userId: userId
            }).$promise;

            yearSummaryData.then(function(response) {
                return response.data;
            }, function(response) {
                $scope.errorresource = response.data;
                flash.pop({
                    title: '',
                    body: $scope.errorresource.message,
                    type: 'alert-danger'
                });
            });
            return yearSummaryData;
        },
        getCategories: function() {
            return $resource("api/trackexp/getcategories/:Id", {}, {
                getCategories: {
                    method: 'GET',
                    isArray: true
                }
            });
        },
        getExpense: function() {
            return $resource("api/trackexp/getexpense/:Id", {}, {
                getExpense: {
                    method: 'POST'
                }
            });
        },
        getAllExpenses: function() {
            return $resource("api/trackexp/getexpenses/:year/:month", {}, {
                getAllExpenses: {
                    method: 'POST',
                    isArray: true
                }
            });
        },
        getIncome: function() {
                    return $resource("api/trackexp/getincome/:Id", {}, {
                        getIncome: {
                            method: 'POST'
                        }
                    });
                },
                getAllIncomes: function() {
                                    return $resource("api/trackexp/getallincomes/:Id", {}, {
                                        getAllIncomes: {
                                            method: 'POST',
                                            isArray: true
                                        }
                                    });
                                },
        getExpensesByCategory: function() {
            return $resource("api/trackexp/getexpensesbycategory/:year/:month", {}, {
                getExpensesByCategory: {
                    method: 'POST',
                    isArray: true
                }
            });
        },
        getBudget: function() {
            return $resource("api/trackexp/getbudget/:Id", {}, {
                getBudget: {
                    method: 'GET'
                }
            });
        },
        saveCategory: function() {
            return $resource("api/trackexp/savecategory", {}, {
                saveCategory: {
                    method: 'POST'
                }
            });
        },
        saveBudget: function() {
            return $resource("api/trackexp/savebudget", {}, {
                saveBudget: {
                    method: 'POST'
                }
            });
        },
        saveExpense: function() {
            return $resource("api/trackexp/saveexpense", {}, {
                saveExpense: {
                    method: 'POST'
                }
            });
        },
        saveIncome: function() {
                    return $resource("api/trackexp/saveincome", {}, {
                        saveIncome: {
                            method: 'POST'
                        }
                    });
                }
    };
});