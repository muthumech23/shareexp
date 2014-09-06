var loginControllers = angular.module('LoginControllers', []);

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
                            $location.path("/home");
                        },
                        function(response) {
                            FlashService.show("Status Code: " + response.status + " Message: Registeration failed.", "alert-danger");
                            cfpLoadingBar.complete();
                        }

                );
            };
        });