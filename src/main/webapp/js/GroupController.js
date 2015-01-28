'use strict'
var groupControllers = angular.module('GroupControllers', []);

groupControllers.controller('GroupRecentController', function($scope, $stateParams, $state, cfpLoadingBar, SessionService, flash, GroupServices) {

    $scope.groupId = $stateParams.groupId;
    var groupBill = GroupServices.getGroupBills($scope.groupId);

    groupBill.then(function(response) {
	$scope.groupBillsData = response;
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

    $scope.addGroupBill = function(grpId) {
	$state.go('billhome.grpaddbill', {
	    groupId : grpId
	});
    };

    $scope.editGroupBill = function(billId) {
	$state.go('billhome.grpeditbill', {
	    billId : billId
	});
    };
});

groupControllers.controller('GroupController', function($scope, $state, $stateParams, cfpLoadingBar, SessionService, flash, GroupServices) {

    $scope.shareGroup;

    $scope.updatedFriendList = [];

    if (!($stateParams.groupId === null || $stateParams.groupId === "" || $stateParams.groupId === undefined)) {
	$scope.groupId = $stateParams.groupId;
	var group = GroupServices.getGroup($scope.groupId);

	group.then(function(response) {
	    $scope.shareGroup = response;
	    $scope.updatedFriendList = $scope.shareGroup.userIds;
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
    } else {
	$scope.updatedFriendList.push(SessionService.get('userId'));
    }
    var updateSelected = function(action, userId) {
	if (action === 'add' && $scope.updatedFriendList.indexOf(userId) === -1) {
	    $scope.updatedFriendList.push(userId);

	}
	if (action === 'remove' && $scope.updatedFriendList.indexOf(userId) !== -1) {
	    $scope.updatedFriendList.splice($scope.updatedFriendList.indexOf(userId), 1);
	}
    };

    $scope.updateSelection = function($event, userId) {
	var checkbox = $event.target;
	var action = (checkbox.checked ? 'add' : 'remove');
	updateSelected(action, userId);
    };

    $scope.isSelected = function(userId) {
	return $scope.updatedFriendList.indexOf(userId) >= 0;
    };

    $scope.saveGroup = function(group) {

	group.userIds = $scope.updatedFriendList;
	group.email = SessionService.get('userEmail');
	group.userId = SessionService.get('userId');

	cfpLoadingBar.start();
	var saveGroup = GroupServices.addGroup(group);
	saveGroup.then(function(response) {
	    flash.pop({
		title : '',
		body : "Group add/update successfully done.",
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
	}

	);
    };
});

groupControllers.controller('GroupAddBillController', function($scope, $stateParams, $state, cfpLoadingBar, SessionService, flash, GroupServices,
	BillingServices) {
    $scope.addBillData;

    $scope.bill = {};

    $scope.updatedBillSPlitList = [];

    cfpLoadingBar.start();
    if (!($stateParams.groupId === null || $stateParams.groupId === "" || $stateParams.groupId === undefined)) {
	var addBill = BillingServices.addGroupBillPage($stateParams.groupId);
	addBill.then(function(response) {
	    console.log(response);
	    $scope.addBillData = response;

	    $scope.addBillSplits = $scope.addBillData.billSplits;

	    angular.forEach($scope.addBillSplits, function(billsplitInput) {
		if (billsplitInput.userId === SessionService.get('userId')) {

		    $scope.updatedBillSPlitList.push(billsplitInput);
		}
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
	$scope.selectGroup($stateParams.groupId);
    } else {

	var addBill = BillingServices.addBillPage();
	addBill.then(function(response) {
	    $scope.addBillData = response;

	    $scope.addBillSplits = $scope.addBillData.billSplits;
	    angular.forEach($scope.addBillSplits, function(billsplitInput) {
		if (billsplitInput.userId === SessionService.get('userId')) {

		    $scope.updatedBillSPlitList.push(billsplitInput);
		}
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
	$scope.selectGroup(1);
    }

    $scope.bill.splitType = 'equally';

    $scope.bill.groupId = $stateParams.groupId;

    $scope.bill.userPaid = SessionService.get('userId');

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
	    updateSplitAmount();
	}
    };

    var updateSplitEqually = function() {
	var splitcount = $scope.updatedBillSPlitList.length;
	var billAmount = $scope.bill.amount;

	angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
	    if ($scope.bill.userPaid === billsplit.userId) {
		billsplit.amount = billAmount / splitcount;
		billsplit.amountStatus = "C";
	    } else {
		billsplit.amount = billAmount / splitcount;
		billsplit.amountStatus = "D";
	    }

	});
    };

    var updateSplitShare = function() {
	var splitcount = $scope.updatedBillSPlitList.length;
	var billAmount = $scope.bill.amount;

	angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
	    if ($scope.bill.userPaid === billsplit.userId) {
		billsplit.amount = billAmount / splitcount;
		billsplit.amountStatus = "C";
	    } else {
		billsplit.amount = billAmount / splitcount;
		billsplit.amountStatus = "D";
	    }
	});
    };

    var updateSplitExact = function() {
	var splitcount = $scope.updatedBillSPlitList.length;
	var billAmount = $scope.bill.amount;

	angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
	    if ($scope.bill.userPaid === billsplit.userId) {
		billsplit.amount = billAmount / splitcount;
		billsplit.amountStatus = "C";
	    } else {
		billsplit.amount = billAmount / splitcount;
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

    $scope.isOneSelectedFn = function() {

	if (isOneSelected()) {

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

	cfpLoadingBar.start();
	var saveBill = BillingServices.getBillResource(billData);
	saveBill.then(function(response) {
	    flash.pop({
		title : '',
		body : "Group Bill added Successfully.",
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
	}

	);
    };

});

groupControllers.controller('GroupEditBillController', function($scope, $state, $stateParams, cfpLoadingBar, flash, BillingServices) {

    $scope.updatedBillSPlitList = [];

    if (!($stateParams.billId === null || $stateParams.billId === "" || $stateParams.billId === undefined)) {
	var bill = BillingServices.showBillResource($stateParams.billId);

	bill.then(function(response) {
	    $scope.bill = response;
	    var billingDate = $filter('date')($scope.bill.date, 'yyyy-MM-dd');
	    $scope.bill.date = billingDate;

	    $scope.updatedBillSPlitList = $scope.bill.billSplits;

	    if (!($scope.bill.groupId === null || $scope.bill.groupId === "" || $scope.bill.groupId === undefined)) {

		var addBill = BillingServices.addGroupBillPage($scope.bill.groupId);
		addBill.then(function(response) {
		    $scope.addBillData = response;

		    $scope.addBillSplits = $scope.addBillData.billSplits;

		}, function(response) {
		    $scope.errorresource = response.data;
		    flash.pop({
			title : '',
			body : $scope.errorresource.message,
			type : 'alert-danger'
		    });
		});

		$scope.selectGroup($scope.bill.groupId);
	    } else {

		var addBill = BillingServices.addBillPage();
		addBill.then(function(response) {
		    $scope.addBillData = response;

		    $scope.addBillSplits = $scope.addBillData.billSplits;

		}, function(response) {
		    $scope.errorresource = response.data;
		    flash.pop({
			title : '',
			body : $scope.errorresource.message,
			type : 'alert-danger'
		    });
		});

		$scope.selectGroup(1);
	    }

	    angular.forEach($scope.updatedBillSPlitList, function(AddBillsplit) {
		console.log("Add Bill:" + AddBillsplit);
		angular.forEach($scope.addBillSplits, function(split) {
		    console.log("Edit Bill:" + split);
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

    $scope.isOneSelectedFn = function() {

	if (isOneSelected()) {

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

	cfpLoadingBar.start();
	var saveBill = BillingServices.getBillResource(billData);
	saveBill.then(function(response) {
	    flash.pop({
		title : '',
		body : "Group Bill updated Successfully.",
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
