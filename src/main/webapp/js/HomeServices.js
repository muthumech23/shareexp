var homeServices = angular.module('HomeServices', []);

homeServices.factory("FlashService", function($rootScope) {
    return {
        show: function(message, msgType) {
            $rootScope.flash = message;
            $rootScope.msgType = msgType;
        },
        clear: function() {
            $rootScope.flash = "";
            $rootScope.msgType = "";
        }
    };
});



homeServices.factory("CookieService", function($cookieStore) {
    return {
        get: function(key) {
            return $cookieStore.get(key);
        },
        set: function(key, val) {
            return $cookieStore.put(key, val);
        },
        unset: function(key) {
           return $cookieStore.put(key, "");
        }
    };
});


homeServices.factory("SessionService", function() {
    return {
        get: function(key) {
            return sessionStorage.getItem(key);
        },
        set: function(key, val) {
            return sessionStorage.setItem(key, val);
        },
        unset: function(key) {
            return sessionStorage.removeItem(key); 
        }
    };
});


