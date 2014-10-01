'use strict'

var loginServices = angular.module('LoginServices', []);

loginServices.factory("FlashService", function($rootScope) {
    return {
	show : function(message, msgType) {
	    $rootScope.flash = message;
	    $rootScope.msgType = msgType;
	},
	clear : function() {
	    $rootScope.flash = "";
	    $rootScope.msgType = "";
	}
    };
});

loginServices.factory("CookieService", function($cookieStore) {
    return {
	get : function(key) {
	    return $cookieStore.get(key);
	},
	set : function(key, val) {
	    return $cookieStore.put(key, val);
	},
	unset : function(key) {
	    return $cookieStore.put(key, "");
	}
    };
});

loginServices.factory("SessionService", function() {
    return {
	get : function(key) {
	    return sessionStorage.getItem(key);
	},
	set : function(key, val) {
	    return sessionStorage.setItem(key, val);
	},
	unset : function(key) {
	    return sessionStorage.removeItem(key);
	}
    };
});

loginServices.factory('UserServices', function($resource) {

    return $resource('api/user/:Id', {
	userId : '@userid'
    }, {
	update : {
	    method : 'PUT'
	}
    });

});

loginServices.factory("AuthenticationService", function($resource, $sanitize, SessionService) {

    return {
	login : function() {
	    return $resource("api/user/auth/login", {}, {
		login : {
		    method : 'POST'
		}
	    });
	},
	activation : function() {
	    return $resource("api/user/auth/activate", {}, {
		activate : {
		    method : 'POST'
		}
	    });
	},
	regenerateActivation : function() {
	    return $resource("api/user/auth/activationcode", {}, {
		regenerateActivate : {
		    method : 'POST'
		}
	    });
	},
	logout : function() {
	    return $resource("api/user/auth/logout", {}, {
		logout : {
		    method : 'GET'
		}
	    });
	},
	forgot : function() {
	    return $resource("api/user/auth/forgot", {}, {
		forgot : {
		    method : 'POST'
		}
	    });
	},
	isLoggedIn : function() {
	    return SessionService.get('authenticated');
	},
	cacheSession : function(key, value) {
	    return SessionService.set(key, value);
	},
	uncacheSession : function(key) {
	    return SessionService.unset(key);
	},
	sanitizeCredentials : function(credentials) {
	    return {
		email : $sanitize(credentials.email),
		password : $sanitize(credentials.password)
	    };
	}
    };
});
