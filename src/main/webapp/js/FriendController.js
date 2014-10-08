'use strict'
var friendControllers = angular.module('FriendControllers', []);

/* Friends Controller */
friendControllers.controller('FriendController', function($scope, $state, cfpLoadingBar, FriendServices, $modal, flash, SessionService) {

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
		flash.pop({title: '', body: "Your friend added successfully and invite has been sent to the address. ", type: 'alert-success'});
		
		$state.go('billhome.friends', {}, {
		    reload : true
		});
		cfpLoadingBar.complete();
	    }, function(response) {
		$scope.errorresource = response.data;
		flash.pop({title: '', body: $scope.errorresource.message, type: 'alert-danger'});
		cfpLoadingBar.complete();
	    });
	}, function() {

	});
    };

    $scope.EditFriend = function(friend) {

	$scope.friend = friend;
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
		flash.pop({title: '', body: "Your friend details hsa been Updated Successfully.", type: 'alert-success'});
		
		cfpLoadingBar.complete();
	    }, function(response) {
		$scope.errorresource = response.data;
		flash.pop({title: '', body: $scope.errorresource.message, type: 'alert-danger'});
		cfpLoadingBar.complete();
	    });
	}, function() {

	});
    };

    $scope.removeFriend = function(friendId) {
	cfpLoadingBar.start();

	var removeFriend = FriendServices.deleteFriends(friendId);
	removeFriend.then(function(response) {
	    flash.pop({title: '', body: "Friend has been removed from your list Successfully", type: 'alert-success'});
	    $state.go('billhome.friends', {}, {
		reload : true
	    });
	    cfpLoadingBar.complete();
	}, function(response) {
	    $scope.errorresource = response.data;
	    flash.pop({title: '', body: $scope.errorresource.message, type: 'alert-danger'});
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
	$modalInstance.dismiss('cancel');
    };
};