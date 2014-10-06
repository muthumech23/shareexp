'use strict'
var loginControllers = angular.module('LoginControllers', []);

loginControllers.controller('HomeController', function($scope, $log, $location) {
    $scope.imagelists = [ {
	url : "img/background2.jpg?v=1&cache=" + (new Date()).getTime()
    }, {
	url : "img/background3.jpg?v=1&cache=" + (new Date()).getTime()
    }, {
	url : "img/background5.jpg?v=1&cache=" + (new Date()).getTime()
    }, {
	url : "img/background6.jpg?v=1&cache=" + (new Date()).getTime()
    } ];
    $log.info = 'Inside Index Controller';

    $scope.slides = [ {
	image : "img/Friends.gif",
	text : "friends"
    }, {
	image : "img/background2.jpg",
	text : "friends"
    } ];

});

loginControllers.controller('ActivateController', function($scope, SessionService) {

    $scope.userSecure = {};

    $scope.userSecure.userId = SessionService.get('userEmail');

});

loginControllers.controller('ChgpwdController', function($scope, SessionService) {
    $scope.user = {};
    $scope.user.email = SessionService.get('userEmail');
    $scope.user.name = SessionService.get('userName');

});

/* Login Controller */
loginControllers
	.controller(
		'LoginController',
		function($scope, $state, cfpLoadingBar, UserServices, flash) {

		    $scope.title = 'Share Expenses - Login';

		    $scope.registerUser = function(user) {

			if (user.password !== $scope.confirmpassword) {
			    flash.pop({
				title : '',
				body : 'Password and Confirm password are different. Please correct it.',
				type : 'alert-danger'
			    });
			    return;
			} else {
			    cfpLoadingBar.start();
			    var userAdd = UserServices.save(user).$promise;
			    userAdd
				    .then(
					    function(response) {
						flash.set({
							    title : '',
							    body : "Register has been completed successfully! Activation code has been sent your Inbox (please check your spam in case not available in inbox). Please enter activation code to activate your account.",
							    type : 'alert-success'
							});
						$state.go("home.activation");
					    }, function(response) {
						$scope.errorresource = response.data;
						flash.pop({
						    title : '',
						    body : $scope.errorresource.code + ": " + $scope.errorresource.message,
						    type : 'alert-danger'
						});
						cfpLoadingBar.complete();
					    }

				    );

			}

		    };

		    $scope.saveUser = function(user) {
			cfpLoadingBar.start();
			var userAdd = UserServices.save(user).$promise;
			userAdd.then(function(response) {
			    flash.pop({
				title : '',
				body : "Your password has been updated successfully",
				type : 'alert-success'
			    });
			}, function(response) {
			    $scope.errorresource = response.data;
			    flash.pop({
				title : '',
				body : $scope.errorresource.code + ": " + $scope.errorresource.message,
				type : 'alert-danger'
			    });
			    cfpLoadingBar.complete();
			}

			);
		    };
		});

/* Login Controller */
loginControllers.controller('UpdateUserController', function($scope, SessionService, cfpLoadingBar, UserServices, flash) {

    var user = SessionService.get('userId');
    var getUser = UserServices.get({
	Id : user
    }).$promise;
    getUser.then(function(response) {
	$scope.user = response;
    }, function(response) {
	$scope.errorresource = response.data;
	flash.pop({
	    title : '',
	    body : $scope.errorresource.code + ": " + $scope.errorresource.message,
	    type : 'alert-danger'
	});
	cfpLoadingBar.complete();
    }

    );

    $scope.saveUser = function(user) {

	cfpLoadingBar.start();
	var userAdd = UserServices.save(user).$promise;

	userAdd.then(function(response) {
	    flash.pop({
		title : '',
		body : "Your password has been updated successfully.",
		type : 'alert-success'
	    });
	}, function(response) {
	    $scope.errorresource = response.data;
	    flash.pop({
		title : '',
		body : $scope.errorresource.code + ": " + $scope.errorresource.message,
		type : 'alert-danger'
	    });
	    cfpLoadingBar.complete();
	}

	);
    };
});