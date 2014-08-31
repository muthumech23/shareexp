var friendControllers = angular.module('FriendControllers', []);

/* Friends Controller */
friendControllers.controller('FriendController',
        function($scope, $log, $location, cfpLoadingBar, FriendServices, $modal, FlashService, friendsData, SessionService) {

            $scope.friends = friendsData;

            $scope.AddFriend = function() {
                $scope.friend = {};
                var modalInstance = $modal.open({
                    templateUrl: 'friendmodal.html',
                    controller: ModalInstanceCtrl,
                    resolve: {
                        friendData: function() {
                            return $scope.friend;
                        }
                    }
                });

                modalInstance.result.then(function(friend) {
                    cfpLoadingBar.start();

                    var user = SessionService.get('userId');

                    friend.friends = [user];
                    var friendAdd = FriendServices.addFriends(friend);
                    friendAdd.then(
                            function(response) {
                                FlashService.show("Added Successfully", "alert-success");
                                $scope.friends.push(response);
                                cfpLoadingBar.complete();
                            },
                            function(response) {
                                FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
                                cfpLoadingBar.complete();
                            }

                    );
                }, function() {
                    
                });
            };

            $scope.EditFriend = function(friend) {

                $scope.friend = friend;
                console.log(friend);
                var modalInstance = $modal.open({
                    templateUrl: 'friendmodal.html',
                    controller: ModalInstanceCtrl,
                    resolve: {
                        friendData: function() {
                            return $scope.friend;
                        }
                    }
                });

                modalInstance.result.then(function(friend) {
                    cfpLoadingBar.start();

                    var friendAdd = FriendServices.editFriends(friend);
                    friendAdd.then(
                            function(response) {
                                FlashService.show("Updated Successfully", "alert-success");
                                cfpLoadingBar.complete();
                            },
                            function(response) {
                                FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
                                cfpLoadingBar.complete();
                            }

                    );
                }, function() {
                    
                });
            };

            $scope.removeFriend = function(friend) {
                cfpLoadingBar.start();
                var removeFriend = FriendServices.deleteFriends(friend.id);
                removeFriend.then(
                        function(response) {
                            FlashService.show("Updated Successfully", "alert-success");
                            var index = $scope.friends.indexOf(friend);

                            if (index !== -1) {
                                // Remove todo-item from array
                                $scope.friends.splice(index, 1);
                            }
                        },
                        function(response) {
                            FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
                            cfpLoadingBar.complete();
                        }

                );
            };

        });

var ModalInstanceCtrl = function($scope, $modalInstance, friendData) {

    $scope.friendData = friendData;

    $scope.ok = function() {
        $modalInstance.close($scope.friendData);

    };

    $scope.cancel = function() {
        console.log('inside');
        $modalInstance.dismiss('cancel');
    };
};