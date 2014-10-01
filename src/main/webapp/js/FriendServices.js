'use strict'
var friendServices = angular.module('FriendServices', []);

friendServices.factory('FriendServices', function($resource, FlashService, SessionService) {

    var friendRes = $resource('api/friend/:Id', {
	friendId : '@friendid'
    }, {
	update : {
	    method : 'PUT'
	}
    });
    var friendRes1 = $resource('api/friend/remove/:Id', {
	friendId : '@friendid'
    }, {
	removeUser : {
	    method : 'POST'
	}
    });
    var userId = SessionService.get('userId');

    return {
	getFriends : function() {

	    var friendCustom = $resource('api/friend/all/:Id', {
		friendId : '@friendid'
	    });

	    var friends = friendCustom.query({
		Id : userId
	    }).$promise;
	    friends.then(function(response) {
		return response.data;
	    }, function(response) {
		$scope.errorresource = response.data;
		    FlashService.show($scope.errorresource.code + ": " + $scope.errorresource.message, 'alert-danger');
	    });
	    return friends;
	},
	addFriends : function(friend, user) {
	    var friends = friendRes.save({
		Id : user
	    }, friend).$promise;
	    return friends;
	},
	editFriends : function(friend) {
	    var friends = friendRes.update(friend).$promise;
	    return friends;
	},
	deleteFriends : function(friendId) {
	    console.log(friendId);
	    var friends = friendRes1.removeUser({
		Id : friendId
	    }, userId).$promise;
	    return friends;
	}
    };
});
