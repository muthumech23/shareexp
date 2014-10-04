'use strict'
var shareExpApp = angular.module("ShareExpApp", [ 'ui.router', 'ngSanitize', 'ngRoute', 'chieffancypants.loadingBar', 'ngAnimate', 'ngResource',
	'ngCookies', 'LoginControllers', 'FriendControllers', 'BillControllers', 'GroupControllers', 'LoginServices', 'FriendServices',
	'BillServices', 'GroupServices', 'ShareExpDirectives', 'ShareExpFilters', 'ui.bootstrap', 'checklist-model' ]);

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
    }).state('privacy', {
	url : '/privacy',
	templateUrl : 'template/privacy.html'
    }).state('terms', {
	url : '/terms',
	templateUrl : 'template/terms.html'
    }).state('home.forgot', {
	url : '/forgot',
	controller : 'LoginController',
	templateUrl : 'template/forgotpwd.html'
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
    }).state('about', {
	url : '/about',
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
	templateUrl : 'template/billhome.edit.html',
	resolve : {
	    addBill : function(BillingServices) {
		return BillingServices.addBillPage();
	    }
	}
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
	templateUrl : 'template/group.editbill.html',
	resolve : {
	    addBill : function(BillingServices) {
		return BillingServices.addBillPage();
	    }
	}
    }).state('billhome.friends', {
	url : '/friends',
	controller : 'FriendController',
	templateUrl : 'template/billhome.friends.html'
    }).state('billhome.account', {
	url : '/account',
	controller : 'UpdateUserController',
	templateUrl : 'template/account.html'
    });
}, function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = true;
});

shareExpApp.controller('IndexController', function($scope, $state, cfpLoadingBar, CookieService, SessionService, AuthenticationService, flash, FlashService,UserServices, $location) {

    $scope.flash = flash;
    
    FlashService.setMainTab(0);
    
    $scope.selectTab = function(setTab) {
	FlashService.setMainTab(setTab);
    };
    $scope.isSelected = function(checkTab) {

	return FlashService.getMainTab() === checkTab;
    };
    
    FlashService.setUserTab(1);

    $scope.selectUser = function(setUser) {
	FlashService.setUserTab(setUser);
    };
    $scope.isUserSelected = function(checkUser) {
	return FlashService.getUserTab() === checkUser;
    };


    $scope.loggedIn = AuthenticationService.isLoggedIn();
    $scope.loggedUser = SessionService.get('userId');
    $scope.loggedUserName = SessionService.get('userName');
    $scope.loggedEmail = SessionService.get('userEmail');

    $scope.userLogin = function(user) {
	cfpLoadingBar.start();
	var sanitizeCredentials = AuthenticationService.sanitizeCredentials(user);

	var userVerify = AuthenticationService.login().login(sanitizeCredentials).$promise;

	userVerify.then(function(response) {
	    $scope.user = response;
	    $scope.userLogin = {};
	    if ($scope.user.status === 'I') {
		flash.set({title: '', body: 'Please enter activation code to activate your account. Activation code has been sent your Inbox (please check your spam in case not available in inbox) during registeration.', type: 'alert-info'});
		AuthenticationService.cacheSession('userEmail', $scope.user.email);
		$scope.loggedEmail = SessionService.get('userEmail');
		cfpLoadingBar.complete();
		$state.go('home.activation');
	    }

	    if ($scope.user.status === 'R') {
		flash.set({title: '', body: 'Please change your password to continue, as your password has been reset recently.', type: 'alert-info'})
		AuthenticationService.cacheSession('userEmail', $scope.user.email);
		AuthenticationService.cacheSession('userName', $scope.user.name);
		$scope.loggedEmail = SessionService.get('userEmail');
		cfpLoadingBar.complete();
		$state.go('home.chgpwd');
	    }

	    if ($scope.user.status === 'A') {
		AuthenticationService.cacheSession('userId', $scope.user.id);
		AuthenticationService.cacheSession('userName', $scope.user.name);
		AuthenticationService.cacheSession('userEmail', $scope.user.email);
		AuthenticationService.cacheSession('authenticated', true);

		CookieService.set('authenticated', true);
		$scope.loggedUser = SessionService.get('userId');
		$scope.loggedIn = AuthenticationService.isLoggedIn();
		$scope.loggedUserName = SessionService.get('userName');
		$scope.loggedEmail = SessionService.get('userEmail');

		$state.go('billhome.list');
	    }
	}, function(response) {
	    $scope.errorresource = response.data;
	    flash.pop({title: '', body: $scope.errorresource.code + ": " + $scope.errorresource.message, type: 'alert-danger'});
	    cfpLoadingBar.complete();
	});
    };

    $scope.activateUser = function(userSecure) {
	cfpLoadingBar.start();

	var userVerify = AuthenticationService.activation().activate(userSecure).$promise;

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

	    $state.go('billhome.list');
	}, function(response) {
	    $scope.errorresource = response.data;
	    flash.pop({title: '', body: $scope.errorresource.code + ": " + $scope.errorresource.message, type: 'alert-danger'});
	    cfpLoadingBar.complete();
	});
    };

    $scope.regenerateactivationCode = function(emailId) {
	cfpLoadingBar.start();

	if (emaildId === null || emailId === '') {
	    flash.pop({title: '', body: $scope.errorresource.code + ": " + $scope.errorresource.message, type: 'alert-danger'});
	}
	var userVerify = AuthenticationService.regenerateActivation().regenerateActivate(emailId).$promise;

	userVerify.then(function(response) {

	    flash.pop({title: '', body: 'Your activation code has been regenerated and sent to your Inbox (please check your spam in case not available in inbox). Please enter activation code to activate your account.', type: 'alert-info'});
	    cfpLoadingBar.complete();

	}, function(response) {
	    $scope.errorresource = response.data;
	    flash.pop({title: '', body: $scope.errorresource.code + ": " + $scope.errorresource.message, type: 'alert-danger'});
	    cfpLoadingBar.complete();
	});
    };

    $scope.forgotPwd = function(emailId) {
	cfpLoadingBar.start();

	var userVerify = AuthenticationService.forgot().forgot(emailId).$promise;

	userVerify.then(function(response) {

	    flash.set({title: '', body: 'Your password has been reset and sent to your Inbox (please check your spam in case not available in inbox). Please login with updated password to continue... ', type: 'alert-info'});
	    $state.go('home');
	    cfpLoadingBar.complete();

	}, function(response) {
	    $scope.errorresource = response.data;
	    flash.pop({title: '', body: $scope.errorresource.code + ": " + $scope.errorresource.message, type: 'alert-danger'});
	    cfpLoadingBar.complete();
	});
    };

    $scope.changePwd = function(user) {
	cfpLoadingBar.start();
	var userAdd = UserServices.update(user).$promise;
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

	    $state.go('billhome.list');
	}, function(response) {
	    $scope.errorresource = response.data;
	    flash.pop({title: '', body: $scope.errorresource.code + ": " + $scope.errorresource.message, type: 'alert-danger'});
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
	    cfpLoadingBar.complete();
	    $location.path('/home');
	}, function(response) {
	    $scope.errorresource = response.data;
	    flash.pop({title: '', body: $scope.errorresource.code + ": " + $scope.errorresource.message, type: 'alert-danger'});
	    cfpLoadingBar.complete();
	});
    };
});

shareExpApp.run(function($rootScope,$state, $location, AuthenticationService, flash, FlashService) {

    var routesThatRequireAuth = [ '/', '', '/home', '/privacy', '/terms', '/about', '/home/signup', '/home/forgot', '/home/chgpwd', "/home/login", '/home/activation' ];
    
    $rootScope.$on('$locationChangeStart', function(event) {

	if (!_(routesThatRequireAuth).contains($location.path()) && !AuthenticationService.isLoggedIn()) {
	    $state.go('home');
	    flash.pop({title: '', body: 'Your are trying to access without login, Please login to continue...', type: 'alert-warning'});
	    return;
	}
    });

    $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams, $scope) {
	if (!_(routesThatRequireAuth).contains($location.path()) && !AuthenticationService.isLoggedIn()) {
	    flash.pop({title: '', body: 'Your are trying to access without login, Please login to continue...', type: 'alert-warning'});
	    $state.go('home');
	}
	
	
    });
    $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams) {
	console.log('$stateChangeError - fired when an error occurs during transition.');
	console.log(arguments);
    });
    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
	
    });

    $rootScope.$on('$viewContentLoaded', function(event) {
	if($location.path().indexOf("/billhome/userlist/") < 0 ){
	    FlashService.setUserTab(1);
	}

    });
    $rootScope.$on('$stateNotFound', function(event, unfoundState, fromState, fromParams) {
	console.log('$stateNotFound ' + unfoundState.to + '  - fired when a state cannot be found by its name.');
	console.log(unfoundState, fromState, fromParams);
    });

});

shareExpApp.factory("flash", function($rootScope) {
    var queue = [], currentMessage = {};
    
    $rootScope.$on('$stateChangeSuccess', function() {
      if (queue.length > 0) 
        currentMessage = queue.shift();
      else
        currentMessage = {};
    });
    
    return {
      set: function(message) {
        var msg = message;
        queue.push(msg);

      },
      get: function(message) {
        return currentMessage;
      },
      pop: function(message) {
        switch(message.type) {
          case 'alert-success':
            toastr.success(message.body, message.title);
            break;
          case 'alert-info':
            toastr.info(message.body, message.title);
            break;
          case 'alert-warning':
            toastr.warning(message.body, message.title);
            break;
          case 'alert-danger':
            toastr.error(message.body, message.title);
            break;
        }
      }
    };
  });

