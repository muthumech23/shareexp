var loginServices = angular.module('LoginServices', []);

loginServices.factory('UserServices', function($resource) {

    return $resource('api/user/:Id', {userId: '@userid'}, {update: {method: 'PUT'}});

});

loginServices.factory("AuthenticationService", function($resource, $sanitize, SessionService) {

    return {
        login: function() {
            return $resource("api/user/auth/login", {}, {login: {method: 'POST'}});
        },
        logout: function() {
            return $resource("api/user/auth/logout", {}, {logout: {method: 'GET'}});
        },
        isLoggedIn: function() {
            return SessionService.get('authenticated');
        },
        cacheSession: function(key, value) {
            return SessionService.set(key, value);
        },
        uncacheSession: function(key) {
            return SessionService.unset(key);
        },
        sanitizeCredentials: function(credentials) {
            return {
                email: $sanitize(credentials.email),
                password: $sanitize(credentials.password)
            };
        }
    };
});


