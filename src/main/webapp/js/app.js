'use strict'
var shareExpApp = angular.module("ShareExpApp", [ 'ui.router', 'ngSanitize',
		'ngRoute', 'chieffancypants.loadingBar', 'ngAnimate', 'ngResource',
		'ngCookies', 'LoginControllers', 'FriendControllers',
		'BillControllers', 'GroupControllers', 'LoginServices',
		'FriendServices', 'BillServices', 'GroupServices',
		'ShareExpDirectives', 'ShareExpFilters', 'ui.bootstrap',
		'checklist-model' ]);

shareExpApp.config(function($stateProvider, $urlRouterProvider) {

	$urlRouterProvider.otherwise('/home');

	$stateProvider.state('home', {
		url : '/home',
		controller : 'HomeController',
		templateUrl : 'template/home.html'
	}).state('home.login', {
		url : '/login',
		controller : 'LoginController',
		templateUrl : 'template/login.html'
	}).state('home.signup', {
		url : '/signup',
		controller : 'LoginController',
		templateUrl : 'template/register.html'
	}).state('home.activation', {
		url : '/activation',
		controller : 'ActivateController',
		templateUrl : 'template/activate.html'
	}).state('home.chgpwd', {
		url : '/chgpwd',
		controller : 'ChgpwdController',
		templateUrl : 'template/changepwd.html'
	}).state('account', {
		url : '/account',
		controller : 'UpdateUserController',
		templateUrl : 'template/account.html'
	}).state('about', {
		url : '/about',
		controller : 'HomeController',
		templateUrl : 'template/aboutus.html'
	}).state('billhome', {
		abstract : true,
		url : '/billhome',
		controller : 'BillListController',
		templateUrl : 'template/billhome.html',
		resolve : {
			homeBillData : function(BillingServices) {
				return BillingServices.getUsersBill();
			}
		}
	}).state('billhome.list', {
		url : '/list',
		controller : 'BillRecentController',
		templateUrl : 'template/billhome.list.html',
		resolve : {
			recentBills : function(BillingServices) {
				return BillingServices.getBills();
			}
		}
	}).state('billhome.userlist', {
		url : '/userlist/:userId',
		controller : 'BillUserController',
		templateUrl : 'template/billhome.userlist.html'
	}).state('billhome.add', {
		url : '/add',
		controller : 'BillAddController',
		templateUrl : 'template/billhome.add.html',
		resolve : {
			addBill : function(BillingServices) {
				return BillingServices.addBillPage();
			}
		}
	}).state('billhome.edit', {
		url : '/edit/:billId',
		controller : 'BillEditController',
		templateUrl : 'template/billhome.edit.html'
	}).state('billhome.grouplist', {
		url : '/grouplist',
		controller : 'GroupListController',
		templateUrl : 'template/group.list.html',
		resolve : {
			getGroupList : function(GroupServices) {
				return GroupServices.getGroups();
			}
		}
	}).state('billhome.groupbills', {
		url : '/groupbills/:groupId',
		controller : 'GroupRecentController',
		templateUrl : 'template/group.bills.html'
	}).state('billhome.groupadd', {
		url : '/groupadd',
		controller : 'GroupController',
		templateUrl : 'template/group.add.html'
	}).state('billhome.grpaddbill', {
		url : '/grpaddbill/:groupId',
		controller : 'GroupAddBillController',
		templateUrl : 'template/group.addbill.html',
		resolve : {
			addBill : function(BillingServices) {
				return BillingServices.addBillPage();
			}
		}
	}).state('billhome.groupedit', {
		url : '/groupedit/:groupId',
		controller : 'GroupController',
		templateUrl : 'template/group.edit.html'
	}).state('billhome.grpeditbill', {
		url : '/grpeditbill/:billId',
		controller : 'GroupEditBillController',
		templateUrl : 'template/group.editbill.html'
	}).state('billhome.friends', {
		url : '/friends',
		controller : 'FriendController',
		templateUrl : 'template/billhome.friends.html'
	});
}, function(cfpLoadingBarProvider) {
	cfpLoadingBarProvider.includeSpinner = true;
});

shareExpApp.controller('IndexController', function($scope, $state, $location, cfpLoadingBar,
		CookieService, SessionService, AuthenticationService, FlashService) {

	$scope.tab = 1;
    
	$scope.selectTab = function (setTab){
		console.log(setTab);
		$scope.tab = setTab;
    };
    $scope.isSelected = function(checkTab) {
    	
        return $scope.tab === checkTab;
    };
    
	$scope.loggedIn = AuthenticationService.isLoggedIn();
	$scope.loggedUser = SessionService.get('userId');
	$scope.loggedUserName = SessionService.get('userName');
	$scope.loggedEmail = SessionService.get('userEmail');

	$scope.userLogin = function(user) {
		cfpLoadingBar.start();
		var sanitizeCredentials = AuthenticationService
				.sanitizeCredentials(user);

		var userVerify = AuthenticationService.login().login(
				sanitizeCredentials).$promise;

		userVerify.then(function(response) {
			$scope.user = response;

			console.log($scope.user);
			console.log($scope.user.status);
			if ($scope.user.status === 'I') {
				FlashService.show('Please provide activation Code to Login',
						'alert-warning');
				AuthenticationService.cacheSession('userEmail',
						$scope.user.email);
				$scope.loggedEmail = SessionService.get('userEmail');
				cfpLoadingBar.complete();
				$state.go('home.activation');
			}

			if ($scope.user.status === 'R') {
				FlashService.show('Please change password', 'alert-danger');
				AuthenticationService.cacheSession('userEmail',
						$scope.user.email);
				$scope.loggedEmail = SessionService.get('userEmail');
				cfpLoadingBar.complete();
				$state.go('home.chgpwd');
			}

			console.log('Outside after IF');
			if ($scope.user.status === 'A') {
				AuthenticationService.cacheSession('userId', $scope.user.id);
				AuthenticationService
						.cacheSession('userName', $scope.user.name);
				AuthenticationService.cacheSession('userEmail',
						$scope.user.email);
				AuthenticationService.cacheSession('authenticated', true);

				CookieService.set('authenticated', true);
				$scope.loggedUser = SessionService.get('userId');
				$scope.loggedIn = AuthenticationService.isLoggedIn();
				$scope.loggedUserName = SessionService.get('userName');
				$scope.loggedEmail = SessionService.get('userEmail');

				$state.go('billhome.list');
			}
		}, function(response) {
			alert(response);
			FlashService.show('Status Code: ' + response.status + response,
					'alert-danger');
			cfpLoadingBar.complete();
		});
	};

	$scope.activateUser = function(userSecure) {
		cfpLoadingBar.start();

		var userVerify = AuthenticationService.activation()
				.activate(userSecure).$promise;

		userVerify.then(function(response) {
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
		}, function(response) {
			alert(response);
			FlashService.show('Status Code: ' + response.status + response,
					'alert-danger');
			cfpLoadingBar.complete();
		});
	};

	$scope.changePwd = function(user) {
		cfpLoadingBar.start();
		var userAdd = UserServices.save(user).$promise;
		userAdd.then(function(response) {
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
		}, function(response) {
			FlashService.show("Status Code: " + response.status
					+ " Message: Registeration failed.", "alert-danger");
			cfpLoadingBar.complete();
		}

		);
	};

	$scope.userLogout = function() {
		cfpLoadingBar.start();
		var userSignout = AuthenticationService.logout().logout().$promise;
		userSignout.then(function(response) {
			AuthenticationService.uncacheSession('userId');
			AuthenticationService.uncacheSession('userName');
			AuthenticationService.uncacheSession('userEmail');
			AuthenticationService.uncacheSession('authenticated');
			CookieService.unset('authenticated');
			$scope.loggedIn = AuthenticationService.isLoggedIn();
			$location.path('/home');
		}, function(response) {
			FlashService.show('Status Code: ' + response.status
					+ ' Message: Logout Failed.', 'alert-danger');
			cfpLoadingBar.complete();
		});
	};
});

shareExpApp.run(function($rootScope, $location, AuthenticationService) {

	var routesThatRequireAuth = [ '/home', '/home/signup', "/home/login",
			'/home/activation' ];

	$rootScope.$on('$locationChangeStart', function(event) {
		if (!_(routesThatRequireAuth).contains($location.path())
				&& !AuthenticationService.isLoggedIn()) {
			$location.path('/home');
			return;
		}
	});
	$rootScope.$on('$routeChangeStart', function(event, next, current) {
		if (!_(routesThatRequireAuth).contains($location.path())
				&& !AuthenticationService.isLoggedIn()) {
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
	$rootScope.$on('$routeChangeError', function(event, current, previous,
			rejection) {
		$rootScope.active = 'progress-danger progress-striped';
		$rootScope.progress = 'failed';
	});

});

shareExpApp.animation('.alert', function($timeout, FlashService) {
	return {
		leave : function(element, done) {
			TweenMax.fromTo(element, 3, {
				opacity : 1
			}, {
				opacity : 0,
				onComplete : done
			});
		},
		enter : function(element, done) {
			TweenMax.fromTo(element, 1, {
				opacity : 0
			}, {
				opacity : 1,
				onComplete : done
			});
			$timeout(function() {
				FlashService.clear();
			}, 3000);
		}
	};
});
