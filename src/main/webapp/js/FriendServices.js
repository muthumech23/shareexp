var friendServices = angular.module('FriendServices', []);

friendServices.factory('FriendServices', function($resource, FlashService, SessionService) {

    var friendRes = $resource('api/friend/:Id', {friendId: '@friendid'}, {update: {method: 'PUT'}});
    var userId = SessionService.get('userId');

    return {
        getFriends: function() {

            var friendCustom = $resource('api/friend/all/:Id', {friendId: '@friendid'});

            var friends = friendCustom.query({Id: userId}).$promise;
            friends.then(function(response) {
                return response.data;
            }, function(response) {
                FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
            });
            return friends;
        },
        addFriends: function(friend, user) {
            var friends = friendRes.save({Id: user}, friend).$promise;
            return friends;
        },
        editFriends: function(friend) {
            var friends = friendRes.update(friend).$promise;
            return friends;
        },
        deleteFriends: function(friendId) {
            var friends = friendRes.remove({Id: friendId}, {userId: userId}).$promise;
            return friends;
        }
    };
});

