var friendServices = angular.module('FriendServices', []);

friendServices.factory('FriendServices', function($resource, FlashService, SessionService) {

    var friendRes = $resource('api/friend/:Id', {friendId: '@friendid'}, {update: {method: 'PUT'}});

    return {
        getFriends: function() {

            var userId = SessionService.get('userId');

            var friendCustom = $resource('api/friends/:Id', {friendId: '@friendid'});

            var friends = friendCustom.query({Id: userId}).$promise;
            friends.then(function(response) {
                return response.data;
            }, function(response) {
                FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
            });
            return friends;
        },
        addFriends: function(friend) {
            var friends = friendRes.save(friend).$promise;
            return friends;
        },
        editFriends: function(friend) {
            var friends = friendRes.update(friend).$promise;
            return friends;
        },
        deleteFriends: function(friendId) {
            var friends = friendRes.remove({Id: friendId}).$promise;
            return friends;
        }

    };
});

