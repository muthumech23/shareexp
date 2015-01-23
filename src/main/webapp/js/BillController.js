'use strict'
var billControllers = angular.module('BillControllers', []);

billControllers.controller('BillListController', function($scope, homeBillData, getGroupList, $state, FriendServices, $modal, flash, SessionService, cfpLoadingBar, BillingServices) {
    
    $scope.oneAtATime = true;
    
    $scope.accordionStatus = {
	    isFirstOpen: true,
	    isFirstDisabled: false
	  };
    
    $scope.usersBillData = homeBillData;

    $scope.userBill = function(userId) {
	$scope.selectUser(userId);
	$state.go('billhome.userlist', {
	    userId : userId
	});
    };
        
    $scope.groupList = getGroupList;
    
    $scope.getGroupBills = function(groupId) {
	$scope.selectGroup(groupId);
	$state.go('billhome.groupbills', {
	    groupId : groupId
	});
    };

    $scope.editGroup = function(groupId) {
	$scope.selectGroup(groupId);
	$state.go('billhome.groupedit', {
	    groupId : groupId
	});
    };
    
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
		
		$state.go('billhome.list', {}, {
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
	    $state.go('billhome.list', {}, {
		reload : true
	    });
	    cfpLoadingBar.complete();
	}, function(response) {
	    $scope.errorresource = response.data;
	    flash.pop({title: '', body: $scope.errorresource.message, type: 'alert-danger'});
	    cfpLoadingBar.complete();
	});
    };
    
    
    $scope.reminderFn = function(userId, loggedUser, amtCurrs) {
	
	$scope.userDto = {'userId': userId, 'loggedUser': loggedUser, 'amtCurrs': amtCurrs };
	console.log($scope.userDto);
	var reminder = BillingServices.reminder($scope.userDto);
	reminder.then(function(response) {
	    flash.pop({title: '', body: "Reminder sent successfully.", type: 'alert-success'});
	   
	}, function(response) {
	    $scope.errorresource = response.data;
	    flash.pop({title: '', body: $scope.errorresource.message, type: 'alert-danger'});
	});
    };
    
    $scope.settleUpFn = function(userId, loggedUser, amtCurrs) {
	
	$scope.settleDto = {'userId': userId, 'loggedUser': loggedUser, 'amtCurrs': amtCurrs };
	
	var modalInstance = $modal.open({
	    templateUrl : 'settlemodal.html',
	    controller : SettleInstanceCtrl,
	    resolve : {
		settleData : function() {
		    return $scope.settleDto;
		}
	    }
	});

	modalInstance.result.then(function(settle) {
	    cfpLoadingBar.start();

		$scope.userDto = {'userId': settle.userId, 'loggedUser': settle.loggedUser,  'amtCurrs': settle.amtCurrUpdated};
		console.log($scope.userDto);
		
		var settle = BillingServices.settleService($scope.userDto);
		settle.then(function(response) {
		    flash.pop({title: '', body: "Amount paid updated successfully.", type: 'alert-success'});
		    $state.go('billhome.list', {}, {
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
    
});

var ModalInstanceCtrl = function($scope, $modalInstance, friendData) {
    
    $scope.avoidSpecialChar = /^[a-zA-Z0-9\s]+$/;

    $scope.friendData = friendData;

    $scope.ok = function() {
	$modalInstance.close($scope.friendData);
    };

    $scope.cancel = function() {
	$modalInstance.dismiss('cancel');
    };
};

var SettleInstanceCtrl = function($scope, $modalInstance, settleData) {
    

    $scope.settleAmtCurr = [];
	
    $scope.updateSettleSelection = function($event, amtCurr) {
	var checkbox = $event.target;
	var action = (checkbox.checked ? 'add' : 'remove');
	updateSettleSelected(action, amtCurr);
    };

    var updateSettleSelected = function(action, amtCurr) {
	if (action === 'add' && $scope.settleAmtCurr.indexOf(amtCurr) === -1) {
	    $scope.settleAmtCurr.push(amtCurr);
	}
	if (action === 'remove' && $scope.settleAmtCurr.indexOf(amtCurr) !== -1) {
	    $scope.settleAmtCurr.splice($scope.settleAmtCurr.indexOf(amtCurr), 1);
	}
    };
    
    $scope.settleData = settleData;

    $scope.ok = function() {
	$scope.settleData.amtCurrUpdated = $scope.settleAmtCurr;
	$modalInstance.close($scope.settleData);
    };

    $scope.cancel = function() {
	$modalInstance.dismiss('cancel');
    };
};

billControllers.controller('BillUserController', function($scope, $state, $stateParams, BillingServices, UserServices, cfpLoadingBar, flash) {

    $scope.editBill = function(billId, userId) {
	$scope.selectUser(userId);
	$state.go('billhome.edit', {
	    billId : billId
	});

    };
    
    $scope.addUserBill = function(userId) {
	$scope.selectUser(userId);
	$state.go('billhome.add', {
	    userId : userId
	});

    };

    if ($stateParams.userId !== null && $stateParams.userId !== "" && $stateParams.userId !== undefined) {

	var userbill = BillingServices.getUserBills($stateParams.userId);

	userbill.then(function(response) {
	    $scope.bills = response;
	    var getUser = UserServices.get({
		Id : $stateParams.userId
	    }).$promise;
	    getUser.then(function(response) {

		$scope.userSelected = response;
		$scope.userSelectedName = $scope.userSelected.name;
	    }, function(response) {

	    });
	    cfpLoadingBar.complete();
	}, function(response) {
	    $scope.errorresource = response.data;
	    flash.pop({
		title : '',
		body : $scope.errorresource.message,
		type : 'alert-danger'
	    });
	    cfpLoadingBar.complete();
	});

    }
});

billControllers.controller('BillRecentController', function($scope, recentBills, $state, $stateParams) {

    $scope.userSelectedName = "All";
    
    $scope.selectGroup(1);
    
    $scope.bills = recentBills;

    $scope.editBill = function(billId) {
	$state.go('billhome.edit', {
	    billId : billId
	});
    };

});

billControllers.controller('BillAddController', function($scope, $state, cfpLoadingBar, flash, BillingServices, addBill, SessionService, $filter, $stateParams) {

    $scope.open = function($event) {
	$event.preventDefault();
	$event.stopPropagation();

	$scope.opened = true;
    };

    $scope.addBillData = addBill;

    $scope.bill = {};

    $scope.addBillSplits = addBill.billSplits;

    $scope.updatedBillSPlitList = [];
    
    angular.forEach($scope.addBillSplits, function(billsplitInput) {
	    if (billsplitInput.userId === SessionService.get('userId')) {
		
		$scope.updatedBillSPlitList.push(billsplitInput);
	    }
	    if (billsplitInput.userId === $stateParams.userId) {
		$scope.selectUser($stateParams.userId);
		$scope.updatedBillSPlitList.push(billsplitInput);
	    }
	    
    });
    console.log($scope.updatedBillSPlitList.length);
    $scope.bill.splitType = 'equally';
    
    $scope.bill.userPaid = SessionService.get('userId');

    $scope.billAmountChng = function() {
	console.log('Inside Bill Amount Change');
	updateSplitAmount();
    };

    var updateSplitAmount = function() {
	if ($scope.bill.splitType === 'equally') {
	    updateSplitEqually();
	} else if ($scope.bill.splitType === 'share') {
	    updateSplitShare();
	} else if ($scope.bill.splitType === 'exact') {
	    console.log('Inside update split Amount');
	    updateSplitExact();
	}
    };

    var updateSelected = function(action, billsplit) {
	if (action === 'add' && $scope.updatedBillSPlitList.indexOf(billsplit) === -1) {
	    $scope.updatedBillSPlitList.push(billsplit);
	    updateSplitAmount();
	}
	if (action === 'remove' && $scope.updatedBillSPlitList.indexOf(billsplit) !== -1) {
	    $scope.updatedBillSPlitList.splice($scope.updatedBillSPlitList.indexOf(billsplit), 1);
	    billsplit.amount = 0;
	    updateSplitAmount();
	}
    };

    var updateSplitEqually = function() {
	var splitcount = $scope.updatedBillSPlitList.length;
	var billAmount = $scope.bill.amount;

	var splittedAmt = billAmount / splitcount;

	angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
	    if ($scope.bill.userPaid === billsplit.userId) {
		billsplit.amount = splittedAmt;
		billsplit.amountStatus = "C";
	    } else {
		billsplit.amount = splittedAmt;
		billsplit.amountStatus = "D";
	    }
	});
    };

    var updateSplitShare = function() {
	var splitcount = 0;
	var billAmount = $scope.bill.amount;

	angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
	    if (billsplit.splitText === "" || billsplit.splitText === null || billsplit.splitText === undefined) {
		splitcount = splitcount + 1;
	    } else {
		splitcount = splitcount + billsplit.splitText;
	    }
	});

	var splittedAmt = billAmount / splitcount;

	angular.forEach($scope.updatedBillSPlitList, function(billsplit) {

	    if (billsplit.splitText === "" || billsplit.splitText === null || billsplit.splitText === undefined) {
		billsplit.amount = splittedAmt * 1;
	    } else {
		billsplit.amount = splittedAmt * billsplit.splitText;
	    }

	    if ($scope.bill.userPaid === billsplit.userId) {
		billsplit.amountStatus = "C";
	    } else {
		billsplit.amountStatus = "D";
	    }
	});
    };

    var updateSplitExact = function() {
	var splitcount = $scope.updatedBillSPlitList.length;
	var billAmount = $scope.bill.amount;

	angular.forEach($scope.updatedBillSPlitList, function(billsplit) {

	    var splittedAmt = billAmount / splitcount;
	    if (billsplit.splitText === "" || billsplit.splitText === null || billsplit.splitText === undefined) {
		billsplit.amount = splittedAmt;
	    } else {

		billsplit.amount = billsplit.splitText;
		billAmount = billAmount - billsplit.splitText;
		splitcount = splitcount - 1;
	    }

	    if ($scope.bill.userPaid === billsplit.userId) {
		billsplit.amountStatus = "C";
	    } else {
		billsplit.amountStatus = "D";
	    }
	});
    };

    $scope.updateSelection = function($event, billsplit) {
	var checkbox = $event.target;
	var action = (checkbox.checked ? 'add' : 'remove');
	updateSelected(action, billsplit);
    };

    $scope.isSelected = function(billsplit) {
	return $scope.updatedBillSPlitList.indexOf(billsplit) >= 0;
    };

    var isOneSelected = function() {

	console.log($scope.updatedBillSPlitList.length);
	if ($scope.updatedBillSPlitList.length === 0) {
	    return true;
	} else if ($scope.updatedBillSPlitList.length === 1) {
	    var selStatus = false; 
	    angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
		if (billsplit.userId === $scope.bill.userPaid) {
		    
		    selStatus = true;
		} 
		console.log(billsplit.userId);
	    });
	    return selStatus;
	} else {
	    return false;
	}
    };
    
    $scope.isOneSelectedFn = function(){
	
	if(isOneSelected()){
	    
	flash.pop({
		title : '',
		body : "Please include one more person other than the user paid.",
		type : 'alert-warning'
	    });
	return true;
	}
	return false;
    };

    $scope.saveBill = function(billData) {

	billData.billSplits = $scope.updatedBillSPlitList;
	billData.by = SessionService.get('userEmail');

	cfpLoadingBar.start();
	var saveBill = BillingServices.getBillResource(billData);
	saveBill.then(function(response) {
	    flash.pop({
		title : '',
		body : "Bill added Successfully.",
		type : 'alert-success'
	    });
	    cfpLoadingBar.complete();
	    $state.go('billhome.list', {}, {
		reload : true
	    });
	}, function(response) {
	    $scope.errorresource = response.data;
	    flash.pop({
		title : '',
		body : $scope.errorresource.message,
		type : 'alert-danger'
	    });
	    cfpLoadingBar.complete();
	});
    };
});

billControllers.controller('BillEditController', function($scope, $state, $filter, $stateParams, cfpLoadingBar, flash, BillingServices, addBill,
	SessionService) {

    $scope.open = function($event) {
	$event.preventDefault();
	$event.stopPropagation();

	$scope.opened = true;
    };

    $scope.addBillData = addBill;

    $scope.addBillSplits = addBill.billSplits;

    $scope.updatedBillSPlitList = [];

    if (!($stateParams.billId === null || $stateParams.billId === "" || $stateParams.billId === undefined)) {
	var bill = BillingServices.showBillResource($stateParams.billId);

	bill.then(function(response) {
	    $scope.bill = response;

	    var billingDate = $filter('date')($scope.bill.date, 'yyyy-MM-dd');
	    $scope.bill.date = billingDate;

	    $scope.updatedBillSPlitList = $scope.bill.billSplits;

	    angular.forEach($scope.updatedBillSPlitList, function(AddBillsplit) {
		angular.forEach($scope.addBillSplits, function(split) {
		    if (AddBillsplit.userId === split.userId) {
			$scope.addBillSplits.splice($scope.addBillSplits.indexOf(split), 1);

			$scope.addBillSplits.push(AddBillsplit);
		    }
		});
	    });
	    cfpLoadingBar.complete();
	}, function(response) {
	    $scope.errorresource = response.data;
	    flash.pop({
		title : '',
		body : $scope.errorresource.message,
		type : 'alert-danger'
	    });
	    cfpLoadingBar.complete();
	});
    }

    $scope.billAmountChng = function() {
	updateSplitAmount();
    };

    var updateSplitAmount = function() {
	if ($scope.bill.splitType === 'equally') {
	    updateSplitEqually();
	} else if ($scope.bill.splitType === 'share') {
	    updateSplitShare();
	} else if ($scope.bill.splitType === 'exact') {
	    updateSplitExact();
	}
    };

    var updateSelected = function(action, billsplit) {
	if (action === 'add' && $scope.updatedBillSPlitList.indexOf(billsplit) === -1) {
	    $scope.updatedBillSPlitList.push(billsplit);
	    updateSplitAmount();
	}
	if (action === 'remove' && $scope.updatedBillSPlitList.indexOf(billsplit) !== -1) {
	    $scope.updatedBillSPlitList.splice($scope.updatedBillSPlitList.indexOf(billsplit), 1);

	    billsplit.amount = 0;
	    updateSplitAmount();

	}
    };

    var updateSplitEqually = function() {
	var splitcount = $scope.updatedBillSPlitList.length;
	var billAmount = $scope.bill.amount;

	var splittedAmt = billAmount / splitcount;

	angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
	    if ($scope.bill.userPaid === billsplit.userId) {
		billsplit.amount = splittedAmt;
		billsplit.amountStatus = "C";
	    } else {
		billsplit.amount = splittedAmt;
		billsplit.amountStatus = "D";
	    }
	});
    };

    var updateSplitShare = function() {
	var splitcount = 0;
	var billAmount = $scope.bill.amount;

	angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
	    if (billsplit.splitText === "" || billsplit.splitText === null || billsplit.splitText === undefined) {
		splitcount = splitcount + 1;
	    } else {
		splitcount = splitcount + billsplit.splitText;
	    }
	});

	var splittedAmt = billAmount / splitcount;

	angular.forEach($scope.updatedBillSPlitList, function(billsplit) {

	    if (billsplit.splitText === "" || billsplit.splitText === null || billsplit.splitText === undefined) {
		billsplit.amount = splittedAmt * 1;
	    } else {
		billsplit.amount = splittedAmt * billsplit.splitText;
	    }

	    if ($scope.bill.userPaid === billsplit.userId) {
		billsplit.amountStatus = "C";
	    } else {
		billsplit.amountStatus = "D";
	    }
	});
    };

    var updateSplitExact = function() {
	var splitcount = $scope.updatedBillSPlitList.length;
	var billAmount = $scope.bill.amount;

	angular.forEach($scope.updatedBillSPlitList, function(billsplit) {

	    var splittedAmt = billAmount / splitcount;
	    if (billsplit.splitText === "" || billsplit.splitText === null || billsplit.splitText === undefined) {
		billsplit.amount = splittedAmt;
	    } else {

		billsplit.amount = billsplit.splitText;
		billAmount = billAmount - billsplit.splitText;
		splitcount = splitcount - 1;
	    }

	    if ($scope.bill.userPaid === billsplit.userId) {
		billsplit.amountStatus = "C";
	    } else {
		billsplit.amountStatus = "D";
	    }

	});
    };

    $scope.updateSelection = function($event, billsplit) {
	var checkbox = $event.target;
	var action = (checkbox.checked ? 'add' : 'remove');
	updateSelected(action, billsplit);
	
    };

    $scope.isSelected = function(billsplit) {
	return $scope.updatedBillSPlitList.indexOf(billsplit) >= 0;

    };

    var isOneSelected = function() {

	console.log($scope.updatedBillSPlitList.length);
	if ($scope.updatedBillSPlitList.length === 0) {
	    return true;
	} else if ($scope.updatedBillSPlitList.length === 1) {
	    var selStatus = false; 
	    angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
		if (billsplit.userId === $scope.bill.userPaid) {
		    
		    selStatus = true;
		} 
		console.log(billsplit.userId);
	    });
	    return selStatus;
	} else {
	    return false;
	}
    };
    
    $scope.isOneSelectedFn = function(){
	
	if(isOneSelected()){
	    
	flash.pop({
		title : '',
		body : "Please include one more person other than the user paid.",
		type : 'alert-warning'
	    });
	return true;
	}
	return false;
    };
    
    $scope.saveBill = function(billData) {

	billData.billSplits = $scope.updatedBillSPlitList;
	billData.by = SessionService.get('userEmail');
	cfpLoadingBar.start();
	var saveBill = BillingServices.getBillResource(billData);
	saveBill.then(function(response) {
	    flash.pop({
		title : '',
		body : 'Bill updated Successfully.',
		type : 'alert-success'
	    });
	    cfpLoadingBar.complete();
	    $state.go('billhome.list', {}, {
		reload : true
	    });
	}, function(response) {
	    $scope.errorresource = response.data;
	    flash.pop({
		title : '',
		body : $scope.errorresource.message,
		type : 'alert-danger'
	    });
	    cfpLoadingBar.complete();
	});
    };
});
