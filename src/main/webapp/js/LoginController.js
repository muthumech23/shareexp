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