'use strict'
var friendControllers = angular.module('FriendControllers', []);

/* Friends Controller */
friendControllers.controller('FriendController', function($scope, $state, cfpLoadingBar, FriendServices, $modal, FlashService, SessionService) {

    $scope.AddFriend = function() {
	$scope.friend = {};
	var modalInstance = $modal.open({
	    templateUrl : 'friendmodal.html',
	    controller : ModalInstanceCtrl,
	    resolve : {
		friendData : function() {
		    return $scope.friend;
		}
	    }
	});

	modalInstance.result.then(function(friend) {
	    cfpLoadingBar.start();

	    var user = SessionService.get('userId');

	    var friendAdd = FriendServices.addFriends(friend, user);
	    friendAdd.then(function(response) {
		FlashService.show("Friend Added Successfully", "alert-success");
		$state.go('billhome.friends', {}, {
		    reload : true
		});
		cfpLoadingBar.complete();
	    }, function(response) {
		$scope.errorresource = response.data;
		    FlashService.show($scope.errorresource.code + ": " + $scope.errorresource.message, 'alert-danger');
		cfpLoadingBar.complete();
	    });
	}, function() {

	});
    };

    $scope.EditFriend = function(friend) {

	$scope.friend = friend;
	console.log(friend);
	var modalInstance = $modal.open({
	    templateUrl : 'friendmodal.html',
	    controller : ModalInstanceCtrl,
	    resolve : {
		friendData : function() {
		    return $scope.friend;
		}
	    }
	});

	modalInstance.result.then(function(friend) {
	    cfpLoadingBar.start();

	    var friendAdd = FriendServices.editFriends(friend);
	    friendAdd.then(function(response) {
		FlashService.show("Updated Successfully", "alert-success");
		cfpLoadingBar.complete();
	    }, function(response) {
		$scope.errorresource = response.data;
		    FlashService.show($scope.errorresource.code + ": " + $scope.errorresource.message, 'alert-danger');
		cfpLoadingBar.complete();
	    });
	}, function() {

	});
    };

    $scope.removeFriend = function(friendId) {
	cfpLoadingBar.start();
	console.log(friendId);

	var removeFriend = FriendServices.deleteFriends(friendId);
	removeFriend.then(function(response) {
	    FlashService.show("Updated Successfully.", "alert-success");
	    $state.go('billhome.friends', {}, {
		reload : true
	    });
	    cfpLoadingBar.complete();
	}, function(response) {
	    $scope.errorresource = response.data;
	    FlashService.show($scope.errorresource.code + ": " + $scope.errorresource.message, 'alert-danger');
	    cfpLoadingBar.complete();
	});
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