var shareExpApp = angular.module(
        "ShareExpApp",
        ["ui.router",
            "ngSanitize",
            "ngRoute",
            'chieffancypants.loadingBar',
            'ngAnimate',
            "ngResource",
            "ngCookies",
            "HomeControllers",
            "FriendControllers",
            "LoginControllers",
            "UserControllers",
            "ShareExpDirectives",
            "HomeServices",
            "LoginServices",
            "FriendServices",
            "BillServices",
            "ShareExpFilters",
            'ui.bootstrap',
            "checklist-model"
            ]);

shareExpApp.config(
        function($stateProvider, $urlRouterProvider) {

            $urlRouterProvider.otherwise('/home');

            $stateProvider
                    .state('home',
                            {url: "/home",
                                controller: "HomeController",
                                templateUrl: "template/home.html"})
                    .state('login',
                            {url: "/login",
                                controller: "LoginController",
                                templateUrl: "template/login.html"})
                    .state('signup',
                            {url: "/signup",
                                controller: "LoginController",
                                templateUrl: "template/register.html"})
                    .state('userhome',
                            {url: "/userhome",
                                controller: "UserController",
                                templateUrl: "template/userhome.html", resolve: {
                                    homeBillData: function(BillingServices) {
                                        return BillingServices.getHomeBills();
                                    }
                                }
                            })
                    .state('friends',
                            {url: "/friends",
                                controller: "FriendController",
                                templateUrl: "template/listfriends.html",
                                resolve: {
                                    friendsData: function(FriendServices) {
                                        return FriendServices.getFriends();
                                    }
                                }
                            })
        },
        function(cfpLoadingBarProvider) {
            cfpLoadingBarProvider.includeSpinner = true;
        });

shareExpApp.controller('IndexController', function($scope, $location, cfpLoadingBar, CookieService, SessionService, AuthenticationService, FlashService) {

    $scope.loggedIn = AuthenticationService.isLoggedIn();
    $scope.loggedUser = SessionService.get('userId');

    $scope.userLogin = function(user) {
        cfpLoadingBar.start();
        var sanitizeCredentials = AuthenticationService.sanitizeCredentials(user);

        var userVerify = AuthenticationService.login().login(sanitizeCredentials).$promise;
        userVerify.then(
                function(response) {
                    $scope.user = response;

                    var userId = $scope.user.id;
                    AuthenticationService.cacheSession('userId', userId);
                    AuthenticationService.cacheSession('authenticated', true);
                    CookieService.set('authenticated', true);
                    $scope.loggedUser = SessionService.get('userId');
                    $scope.loggedIn = AuthenticationService.isLoggedIn();

                    FlashService.show(response, "alert-success");
                    $location.path("/userhome");
                },
                function(response) {
                    FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
                    cfpLoadingBar.complete();
                }
        );
    };

    $scope.userLogout = function() {
        cfpLoadingBar.start();
        var userSignout = AuthenticationService.logout().logout().$promise;
        userSignout.then(
                function(response) {
                    AuthenticationService.uncacheSession('userId');
                    AuthenticationService.uncacheSession('authenticated');
                    CookieService.unset('authenticated');
                    $scope.loggedIn = AuthenticationService.isLoggedIn();
                    $location.path("/home");
                },
                function(response) {
                    FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
                    cfpLoadingBar.complete();
                }
        );
    };
});

shareExpApp.run(
        function($rootScope, $location, AuthenticationService) {

            var routesThatRequireAuth = ['/userhome', '/friends'];

            $rootScope.$on('$locationChangeStart', function(event) {
                if (_(routesThatRequireAuth).contains($location.path()) && !AuthenticationService.isLoggedIn()) {
                    alert('Please Login to COntinue');
                    event.preventDefault();
                    return;
                }
            });
            $rootScope.$on('$routeChangeStart', function(event, next, current) {
                if (_(routesThatRequireAuth).contains($location.path()) && !AuthenticationService.isLoggedIn()) {
                    $location.path('/login');
                    return;
                }
                $rootScope.active = "progress-success progress-striped";
                $rootScope.progress = "Loading...";
            });
            $rootScope.$on("$routeChangeSuccess", function(event, current, previous) {
                $rootScope.active = "progress-success progress-striped";
                $rootScope.progress = "";
                $rootScope.newLocation = $location.path();

            });
            $rootScope.$on("$routeChangeError", function(event, current, previous, rejection) {
                $rootScope.active = "progress-danger progress-striped";
                $rootScope.progress = "failed";
            });

        });


shareExpApp.animation(".alert", function($timeout, FlashService) {
    return {
        leave: function(element, done) {
            TweenMax.fromTo(element, 10, {opacity: 1}, {opacity: 0, onComplete: done});
        },
        enter: function(element, done) {
            TweenMax.fromTo(element, 1, {opacity: 0}, {opacity: 1, onComplete: done});
            $timeout(function() {
                FlashService.clear();
            }, 10000);
        }
    };
});
