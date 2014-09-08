var shareExpApp = angular.module(
        'ShareExpApp',
        ['ui.router',
            'ngSanitize',
            'ngRoute',
            'chieffancypants.loadingBar',
            'ngAnimate',
            'ngResource',
            'ngCookies',
            'HomeControllers',
            'LoginControllers',
            'FriendControllers',
            'BillControllers',
            'GroupControllers',
            'HomeServices',
            'LoginServices',
            'FriendServices',
            'BillServices',
            'GroupServices',
            'ShareExpDirectives',
            'ShareExpFilters',
            'ui.bootstrap',
            'checklist-model'
        ]);

shareExpApp.config(
        function($stateProvider, $urlRouterProvider) {

            $urlRouterProvider.otherwise('/home');

            $stateProvider
                    .state('home',
                            {url: '/home',
                                controller: 'HomeController',
                                templateUrl: 'template/home.html'})
                    .state('signup',
                            {url: '/signup',
                                controller: 'LoginController',
                                templateUrl: 'template/register.html'})
                    .state('account',
                            {url: '/account',
                                controller: 'UpdateUserController',
                                templateUrl: 'template/account.html'})
                    .state('about',
                            {url: '/about',
                                controller: 'HomeController',
                                templateUrl: 'template/aboutus.html'})
                    .state('billhome',
                            {abstract: true,
                                url: '/billhome',
                                controller: 'BillListController',
                                templateUrl: 'template/billhome.html', resolve: {
                                    homeBillData: function(BillingServices) {
                                        return BillingServices.getUsersBill();
                                    }
                                }
                            })
                    .state('billhome.list',
                            {url: '/list',
                                controller: 'BillRecentController',
                                templateUrl: 'template/billhome.list.html', resolve: {
                                    recentBills: function(BillingServices) {
                                        return BillingServices.getBills();
                                    }
                                }
                            })
                    .state('billhome.add',
                            {url: '/add',
                                controller: 'BillAddController',
                                templateUrl: 'template/billhome.add.html', resolve: {
                                    addBill: function(BillingServices) {
                                        return BillingServices.addBillPage();
                                    }
                                }
                            })
                    .state('billhome.edit',
                            {url: '/edit/:billId',
                                controller: 'BillEditController',
                                templateUrl: 'template/billhome.edit.html'})
                    .state('billhome.grouplist',
                            {url: '/grouplist',
                                controller: 'GroupListController',
                                templateUrl: 'template/group.list.html', resolve: {
                                    getGroupList: function(GroupServices) {
                                        return GroupServices.getGroups();
                                    }
                                }
                            })
                    .state('billhome.groupbills',
                            {url: '/groupbills/:groupId',
                                controller: 'GroupRecentController',
                                templateUrl: 'template/group.bills.html'})
                    .state('billhome.groupadd',
                            {url: '/groupadd',
                                controller: 'GroupController',
                                templateUrl: 'template/group.add.html'})
                    .state('billhome.grpaddbill',
                            {url: '/grpaddbill/:groupId',
                                controller: 'GroupAddBillController',
                                templateUrl: 'template/group.addbill.html', resolve: {
                                    addBill: function(BillingServices) {
                                        return BillingServices.addBillPage();
                                    }
                                }
                            })
                    .state('billhome.groupedit',
                            {url: '/groupedit/:groupId',
                                controller: 'GroupController',
                                templateUrl: 'template/group.edit.html'})
                    .state('billhome.grpeditbill',
                            {url: '/grpeditbill/:billId',
                                controller: 'GroupEditBillController',
                                templateUrl: 'template/group.editbill.html'})
                    .state('billhome.friends',
                            {url: '/friends',
                                controller: 'FriendController',
                                templateUrl: 'template/billhome.friends.html'});
        },
        function(cfpLoadingBarProvider) {
            cfpLoadingBarProvider.includeSpinner = true;
        });

shareExpApp.controller('IndexController', function($scope, $location, cfpLoadingBar, CookieService, SessionService, AuthenticationService, FlashService) {

    $scope.loggedIn = AuthenticationService.isLoggedIn();
    $scope.loggedUser = SessionService.get('userId');
    $scope.loggedUserName = SessionService.get('userName');

    $scope.userLogin = function(user) {
        cfpLoadingBar.start();
        var sanitizeCredentials = AuthenticationService.sanitizeCredentials(user);

        var userVerify = AuthenticationService.login().login(sanitizeCredentials).$promise;

        userVerify.then(
                function(response) {
                    $scope.user = response;
                    AuthenticationService.cacheSession('userId', $scope.user.id);
                    AuthenticationService.cacheSession('userName', $scope.user.name);
                    AuthenticationService.cacheSession('userEmail', $scope.user.email);
                    AuthenticationService.cacheSession('authenticated', true);

                    CookieService.set('authenticated', true);
                    $scope.loggedUser = SessionService.get('userId');
                    $scope.loggedIn = AuthenticationService.isLoggedIn();
                    $scope.loggedUserName = SessionService.get('userName');

                    $location.path('/billhome/list');
                },
                function(response) {
                    FlashService.show('Status Code: ' + response.status + ' Message: Login Failed. Please try again.', 'alert-danger');
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
                    AuthenticationService.uncacheSession('userName');
                    AuthenticationService.uncacheSession('userEmail');
                    AuthenticationService.uncacheSession('authenticated');
                    CookieService.unset('authenticated');
                    $scope.loggedIn = AuthenticationService.isLoggedIn();
                    $location.path('/home');
                },
                function(response) {
                    FlashService.show('Status Code: ' + response.status + ' Message: Logout Failed.', 'alert-danger');
                    cfpLoadingBar.complete();
                }
        );
    };
});

shareExpApp.run(
        function($rootScope, $location, AuthenticationService) {

            var routesThatRequireAuth = ['/home', '/signup', "/"];

            $rootScope.$on('$locationChangeStart', function(event) {
                if (!_(routesThatRequireAuth).contains($location.path()) && !AuthenticationService.isLoggedIn()) {
                    alert('Please Login to Continue');
                    $location.path('/home');
                    return;
                }
            });
            $rootScope.$on('$routeChangeStart', function(event, next, current) {
                if (_(routesThatRequireAuth).contains($location.path()) && !AuthenticationService.isLoggedIn()) {
                    $location.path('/home');
                    return;
                }
                $rootScope.active = 'progress-success progress-striped';
                $rootScope.progress = 'Loading...';
            });
            $rootScope.$on('$routeChangeSuccess', function(event, current, previous) {
                $rootScope.active = 'progress-success progress-striped';
                $rootScope.progress = '';
                $rootScope.newLocation = $location.path();

            });
            $rootScope.$on('$routeChangeError', function(event, current, previous, rejection) {
                $rootScope.active = 'progress-danger progress-striped';
                $rootScope.progress = 'failed';
            });

        });


shareExpApp.animation('.alert', function($timeout, FlashService) {
    return {
        leave: function(element, done) {
            TweenMax.fromTo(element, 5, {opacity: 1}, {opacity: 0, onComplete: done});
        },
        enter: function(element, done) {
            TweenMax.fromTo(element, 1, {opacity: 0}, {opacity: 1, onComplete: done});
            $timeout(function() {
                FlashService.clear();
            }, 5000);
        }
    };
});
