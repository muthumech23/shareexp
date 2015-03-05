'use strict'
var trackExpController = angular.module('TrackExpController', []);


trackExpController.controller('TrackControllerHome', function($scope, $filter, ExpenseServices, FlashService, cfpLoadingBar, flash, SessionService, getYearSummary, $state) {

	$scope.status = {
        isFirstOpen: true,
        isSummaryOpen: true,
        isFirstDisabled: false
    };

    $scope.yearSummaryData = getYearSummary;

    $scope.currentDate = new Date();
    var currentLongDate = $filter('date')(new Date(), 'longDate');
    $scope.currentYear = $filter('date')(new Date(), 'yyyy');
    $scope.currentMonth = $filter('date')(new Date(), 'MM');

    $scope.track = {};
    $scope.categories = [];
    $scope.budget = [];
    $scope.expense = [];
    $scope.income = [];
    $scope.allIncomes = [];
    $scope.allExpenses = [];

    $scope.expensesByCategory = [];

    $scope.months = [{
        mm: '01',
        name: 'JAN'
    }, {
        mm: '02',
        name: 'FEB'
    }, {
        mm: '03',
        name: 'MAR'
    }, {
        mm: '04',
        name: 'APR'
    }, {
        mm: '05',
        name: 'MAY'
    }, {
        mm: '06',
        name: 'JUN'
    }, {
        mm: '07',
        name: 'JUL'
    }, {
        mm: '08',
        name: 'AUG'
    }, {
        mm: '09',
        name: 'SEP'
    }, {
        mm: '10',
        name: 'OCT'
    }, {
        mm: '11',
        name: 'NOV'
    }, {
        mm: '12',
        name: 'DEC'
    }];

    var loggedUserId = SessionService.get('userId');

    var currentDayExpense = function(dateEntered) {
        cfpLoadingBar.start();
        var getExpense = ExpenseServices.getExpense().getExpense({
            Id: loggedUserId
        }, dateEntered).$promise;

        getExpense
            .then(function(response) {
                    console.log("Inside get Expense:" + response);
                    $scope.expense = response;
                    cfpLoadingBar.complete();
                }, function(response) {
                    $scope.errorresource = response.data;
                    flash.pop({
                        title: '',
                        body: $scope.errorresource.message,
                        type: 'alert-danger'
                    });
                    cfpLoadingBar.complete();
                }

            );
    }

    var currentDayIncome = function(dateEntered) {
        cfpLoadingBar.start();
        var getIncome = ExpenseServices.getIncome().getIncome({
            Id: loggedUserId
        }, dateEntered).$promise;

        getIncome
            .then(function(response) {
                    console.log("Inside get income:" + response);
                    $scope.income = response;
                    cfpLoadingBar.complete();
                }, function(response) {
                    $scope.errorresource = response.data;
                    flash.pop({
                        title: '',
                        body: $scope.errorresource.message,
                        type: 'alert-danger'
                    });
                    cfpLoadingBar.complete();
                }

            );
    }

    var getCurrentMonthAllExpenses = function(currentYear, currentMonth) {

        cfpLoadingBar.start();
        var getAllExpenses = ExpenseServices.getAllExpenses().getAllExpenses({
            year: currentYear,
            month: currentMonth
        }, loggedUserId).$promise;

        getAllExpenses
            .then(function(response) {
                    console.log("Inside get All Expense:" + response);
                    $scope.allExpenses = response;
                    cfpLoadingBar.complete();
                }, function(response) {
                    $scope.errorresource = response.data;
                    flash.pop({
                        title: '',
                        body: $scope.errorresource.message,
                        type: 'alert-danger'
                    });
                    cfpLoadingBar.complete();
                }

            );
    }

    var getCurrentMonthAllIncomes = function(dateEntered) {

        cfpLoadingBar.start();
        var getIncomes = ExpenseServices.getAllIncomes().getAllIncomes({
            Id: loggedUserId
        }, dateEntered).$promise;

        getIncomes
            .then(function(response) {
                    console.log("Inside get income:" + response);
                    $scope.allIncomes = response;
                    cfpLoadingBar.complete();
                }, function(response) {
                    $scope.errorresource = response.data;
                    flash.pop({
                        title: '',
                        body: $scope.errorresource.message,
                        type: 'alert-danger'
                    });
                    cfpLoadingBar.complete();
                }

            );
    }

    var getCurrentMonthAllExpensesByCategory = function(currentYear, currentMonth) {

        cfpLoadingBar.start();
        var getAllExpensesByCategory = ExpenseServices.getExpensesByCategory().getExpensesByCategory({
            year: currentYear,
            month: currentMonth
        }, loggedUserId).$promise;

        getAllExpensesByCategory
            .then(function(response) {
                    console.log("Inside get All Expense:" + response);
                    $scope.expensesByCategory = response;
                    cfpLoadingBar.complete();
                }, function(response) {
                    $scope.errorresource = response.data;
                    flash.pop({
                        title: '',
                        body: $scope.errorresource.message,
                        type: 'alert-danger'
                    });
                    cfpLoadingBar.complete();
                }

            );
    }

    $scope.track.month = $scope.currentMonth;
    $scope.track.year = $scope.currentYear;
    $scope.loggedUserId = loggedUserId;

    currentDayExpense(currentLongDate);
    getCurrentMonthAllExpenses($filter('date')(new Date(), 'yyyy'), $filter('date')(new Date(), 'MM'));
    getCurrentMonthAllExpensesByCategory($filter('date')(new Date(), 'yyyy'), $filter('date')(new Date(), 'MM'));

    $scope.getExpenseCurrent = function() {
        currentDayExpense(currentLongDate);
    }

    $scope.getAllIncome = function() {
        getCurrentMonthAllIncomes(currentLongDate);
    }

    $scope.getAllExpenses = function(year, month) {
        getCurrentMonthAllExpenses(year, month);

    }

    $scope.getAllExpensesByCategory = function(year, month) {
        getCurrentMonthAllExpensesByCategory(year, month);

    }

    $scope.getExpense = function(dateEntered) {
        currentDayExpense(dateEntered);
    }

    $scope.getIncome = function(dateEntered) {
        currentDayIncome(dateEntered);
    }

    $scope.functionGetExpenses = function() {
        console.log($scope.track.year + '|' + $scope.track.month)
        getCurrentMonthAllExpenses($scope.track.year, $scope.track.month);
        getCurrentMonthAllExpensesByCategory($scope.track.year, $scope.track.month);
    }

    $scope.functionMonthExpenses = function(month) {
        console.log($scope.track.year + '|' + month)
        getCurrentMonthAllExpenses($scope.track.year, month);
        getCurrentMonthAllExpensesByCategory($scope.track.year, month);
    }

    $scope.getCategory = function() {

        cfpLoadingBar.start();
        var getCategories = ExpenseServices.getCategories().getCategories({
            Id: loggedUserId
        }).$promise;

        getCategories
            .then(function(response) {
                    console.log("Inside get Categories:" + response);
                    $scope.categories = response;
                    cfpLoadingBar.complete();
                }, function(response) {
                    $scope.errorresource = response.data;
                    flash.pop({
                        title: '',
                        body: $scope.errorresource.message,
                        type: 'alert-danger'
                    });
                    cfpLoadingBar.complete();
                }

            );
    }


    $scope.getBudget = function() {

        cfpLoadingBar.start();
        var getBudget = ExpenseServices.getBudget().getBudget({
            Id: loggedUserId
        }).$promise;

        getBudget
            .then(function(response) {
                    console.log("Inside get budget:" + response);
                    $scope.budget = response;
                    cfpLoadingBar.complete();
                }, function(response) {
                    $scope.errorresource = response.data;
                    flash.pop({
                        title: '',
                        body: $scope.errorresource.message,
                        type: 'alert-danger'
                    });
                    cfpLoadingBar.complete();
                }

            );
    }

    $scope.addCategory = function(saveCty) {

        cfpLoadingBar.start();
        saveCty.userId = loggedUserId;
        console.log("Inside save Categories:" + saveCty.userId);
        console.log("Inside save Categories:" + saveCty.category);
        var categoryAdd = ExpenseServices.saveCategory().saveCategory(saveCty).$promise;

        categoryAdd.then(function(response) {
                $scope.categories.push(response);
                flash.pop({
                    title: '',
                    body: "Category Added successfully.",
                    type: 'alert-success'
                });
                cfpLoadingBar.complete();
                $state.go('trackexp', {}, {reload: true});
            }, function(response) {
                $scope.errorresource = response.data;
                flash.pop({
                    title: '',
                    body: $scope.errorresource.message,
                    type: 'alert-danger'
                });
                cfpLoadingBar.complete();
            }

        );
    };

    $scope.saveBudget = function(budgetToSave) {

        cfpLoadingBar.start();
        console.log("Inside save saveBudget:" + budgetToSave);
        var categoryAdd = ExpenseServices.saveBudget().saveBudget(budgetToSave).$promise;

        categoryAdd.then(function(response) {
                flash.pop({
                    title: '',
                    body: "Budget Added successfully.",
                    type: 'alert-success'
                });
                cfpLoadingBar.complete();
                $state.go('trackexp', {}, {reload: true});
            }, function(response) {
                $scope.errorresource = response.data;
                flash.pop({
                    title: '',
                    body: $scope.errorresource.message,
                    type: 'alert-danger'
                });
                cfpLoadingBar.complete();
            }

        );
    };

    $scope.saveExpense = function(expenseToSave) {

        cfpLoadingBar.start();
        console.log("Inside save expenseToSave:" + expenseToSave);
        var expenseAdd = ExpenseServices.saveExpense().saveExpense(expenseToSave).$promise;

        expenseAdd.then(function(response) {
                flash.pop({
                    title: '',
                    body: "Expense Added successfully.",
                    type: 'alert-success'
                });
                cfpLoadingBar.complete();
                $state.go('trackexp', {}, {reload: true});
            }, function(response) {
                $scope.errorresource = response.data;
                flash.pop({
                    title: '',
                    body: $scope.errorresource.message,
                    type: 'alert-danger'
                });
                cfpLoadingBar.complete();
            }

        );
    };

    $scope.saveIncome = function(incomeToSave) {

        cfpLoadingBar.start();
        console.log("Inside save expenseToSave:" + incomeToSave);
        var incomeAdd = ExpenseServices.saveIncome().saveIncome(incomeToSave).$promise;

        incomeAdd.then(function(response) {
                flash.pop({
                    title: '',
                    body: "Expense Added successfully.",
                    type: 'alert-success'
                });
                cfpLoadingBar.complete();
                $state.go('trackexp', {}, {reload: true});
            }, function(response) {
                $scope.errorresource = response.data;
                flash.pop({
                    title: '',
                    body: $scope.errorresource.message,
                    type: 'alert-danger'
                });
                cfpLoadingBar.complete();
            }

        );
    };

});