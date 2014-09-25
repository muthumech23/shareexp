'use strict'
var loginControllers = angular.module('LoginControllers', []);

loginControllers.controller('HomeController',
        function($scope, $log, $location) {
            $scope.imagelists = [{url: "img/background2.jpg?v=1&cache=" + (new Date()).getTime()}, {url: "img/background3.jpg?v=1&cache=" + (new Date()).getTime()}, {url: "img/background5.jpg?v=1&cache=" + (new Date()).getTime()}, {url: "img/background6.jpg?v=1&cache=" + (new Date()).getTime()}];
            $log.info = 'Inside Index Controller';

            $scope.slides = [{image: "img/Friends.gif", text: "friends"}, {image: "img/background2.jpg", text: "friends"}];

        });

loginControllers.controller('ActivateController',
        function($scope, SessionService) {
	
	$scope.userSecure = {};
	
	$scope.userSecure.userId = SessionService.get('userEmail');

        });

loginControllers.controller('ChgpwdController',
        function($scope, SessionService) {
	$scope.user = {};
	$scope.user.email = SessionService.get('userEmail');
	$scope.user.name = SessionService.get('userName');

        });

/* Login Controller */
loginControllers.controller('LoginController',
        function($scope, $location, cfpLoadingBar, UserServices, FlashService) {

            $scope.title = 'Share Expenses - Login';

            $scope.registerUser = function(user) {
                cfpLoadingBar.start();
                var userAdd = UserServices.save(user).$promise;
                userAdd.then(
                        function(response) {
                            FlashService.show("Register completed successfully! please login to continue...", "alert-success");
                            $location.path("/home/activation");
                        },
                        function(response) {
                            FlashService.show("Status Code: " + response.status + " Message: Registeration failed.", "alert-danger");
                            cfpLoadingBar.complete();
                        }

                );
            };

            $scope.saveUser = function(user) {
                cfpLoadingBar.start();
                var userAdd = UserServices.save(user).$promise;
                userAdd.then(
                        function(response) {
                            FlashService.show("Password updated successfully", "alert-success");
                        },
                        function(response) {
                            FlashService.show("Status Code: " + response.status + " Message: Registeration failed.", "alert-danger");
                            cfpLoadingBar.complete();
                        }

                );
            };
        });


/* Login Controller */
loginControllers.controller('UpdateUserController',
        function($scope, SessionService, cfpLoadingBar, UserServices, FlashService) {

            var user = SessionService.get('userId');
            var getUser = UserServices.get({Id: user}).$promise;
            getUser.then(
                    function(response) {
                        $scope.user = response;
                    },
                    function(response) {
                        FlashService.show("Status Code: " + response.status + " Message: Registeration failed.", "alert-danger");
                        cfpLoadingBar.complete();
                    }

            );
    
            $scope.saveUser = function(user) {
                cfpLoadingBar.start();
                var userAdd = UserServices.save(user).$promise;
                userAdd.then(
                        function(response) {
                            FlashService.show("Password updated successfully", "alert-success");
                        },
                        function(response) {
                            FlashService.show("Status Code: " + response.status + " Message: Registeration failed.", "alert-danger");
                            cfpLoadingBar.complete();
                        }

                );
            };
        });