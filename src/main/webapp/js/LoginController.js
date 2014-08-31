var loginControllers = angular.module('LoginControllers', []);

/* Login Controller */
loginControllers.controller('LoginController',
        function($scope, $log, $location, cfpLoadingBar, UserServices, AuthenticationService, CookieService, FlashService) {

            $scope.title = 'Share Expenses - Login';

            $scope.registerUser = function(user) {
                cfpLoadingBar.start();
                var userAdd = UserServices.save(user).$promise;
                userAdd.then(
                        function(response) {
                            FlashService.show(response, "alert-success");
                            $location.path("/login");
                        },
                        function(response) {
                            FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
                            cfpLoadingBar.complete();
                        }

                );
            };
        });