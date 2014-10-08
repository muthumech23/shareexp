'use strict'
var groupControllers = angular.module('GroupControllers', []);

groupControllers.controller('GroupListController', function($scope, $log, $state, cfpLoadingBar, SessionService, flash, GroupServices, getGroupList) {

    $scope.groupList = getGroupList;

    $scope.getGroupBills = function(groupId) {
	$state.go('billhome.groupbills', {
	    groupId : groupId
	});
    };

    $scope.editGroup = function(groupId) {
	$state.go('billhome.groupedit', {
	    groupId : groupId
	});
    };

});

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
	    $state.go('billhome.grouplist', {}, {
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
	addBill) {

    $scope.addBillData = addBill;

    $scope.bill = {};

    $scope.addBillData.groupId = $stateParams.groupId;

    $scope.addBillSplits = addBill.billSplits;

    $scope.updatedBillSPlitList = [];

    $scope.bill.splitType = 'equally';
    $scope.billAmountChng = function() {
	updateSplitAmount();
    };
    $scope.open = function($event) {
	$event.preventDefault();
	$event.stopPropagation();

	$scope.opened = true;
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
	    } else {
		billsplit.amount = -(billAmount / splitcount);
	    }

	});
    };

    var updateSplitShare = function() {
	var splitcount = $scope.updatedBillSPlitList.length;
	var billAmount = $scope.bill.amount;

	angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
	    if ($scope.bill.userPaid === billsplit.userId) {
		billsplit.amount = billAmount / splitcount;
	    } else {
		billsplit.amount = -(billAmount / splitcount);
	    }
	});
    };

    var updateSplitExact = function() {
	var splitcount = $scope.updatedBillSPlitList.length;
	var billAmount = $scope.bill.amount;

	angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
	    if ($scope.bill.userPaid === billsplit.userId) {
		billsplit.amount = billAmount / splitcount;
	    } else {
		billsplit.amount = -(billAmount / splitcount);
	    }
	});
    };

    $scope.updateSelection = function($event, billsplit) {
	var checkbox = $event.target;
	var action = (checkbox.checked ? 'add' : 'remove');
	updateSelected(action, billsplit);
	isOneSelected();
    };

    $scope.isSelected = function(billsplit) {
	return $scope.updatedBillSPlitList.indexOf(billsplit) >= 0;
    };

    var isOneSelected = function() {
	if ($scope.updatedBillSPlitList.length === 0) {
	    return true;
	} else if ($scope.updatedBillSPlitList.length === 1) {
	    var saveStatus = false;
	    angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
		if (billsplit.userId === $scope.bill.userPaid) {
		    flash.pop({
			title : '',
			body : "Please include one more person other than the user paid.",
			type : 'alert-warning'
		    });
		    saveStatus = true;
		} else {
		    saveStatus = false;
		}
	    });
	    return saveStatus;
	} else {
	    return false;
	}
    };

    $scope.saveBill = function(billData) {
	if ($scope.updatedBillSPlitList.length === 1) {
	    angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
		if (billsplit.userId === billData.userPaid) {
		    flash.pop({
			title : '',
			body : "Please include one more person other than the user paid.",
			type : 'alert-warning'
		    });
		    return;
		}
	    });
	}
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

groupControllers.controller('GroupEditBillController', function($scope, $state, $stateParams, addBill, cfpLoadingBar, flash, BillingServices) {

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
    $scope.open = function($event) {
	$event.preventDefault();
	$event.stopPropagation();

	$scope.opened = true;
    };
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
	    } else {
		billsplit.amount = -splittedAmt;
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
	    } else {
		billsplit.amount = -billsplit.amount;
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
	    } else {
		billsplit.amount = -billsplit.amount;
	    }
	});
    };

    $scope.updateSelection = function($event, billsplit) {
	var checkbox = $event.target;
	var action = (checkbox.checked ? 'add' : 'remove');
	updateSelected(action, billsplit);
	isOneSelected();
    };

    $scope.isSelected = function(billsplit) {
	return $scope.updatedBillSPlitList.indexOf(billsplit) >= 0;
    };

    var isOneSelected = function() {
	if ($scope.updatedBillSPlitList.length === 0) {
	    return true;
	} else if ($scope.updatedBillSPlitList.length === 1) {
	    var saveStatus = false;
	    angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
		if (billsplit.userId === $scope.bill.userPaid) {
		    flash.pop({
			title : '',
			body : "Please include one more person other than the user paid.",
			type : 'alert-warning'
		    });
		    saveStatus = true;
		} else {
		    saveStatus = false;
		}
	    });
	    return saveStatus;
	} else {
	    return false;
	}
    };

    $scope.saveBill = function(billData) {

	if ($scope.updatedBillSPlitList.length === 1) {
	    angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
		if (billsplit.userId === billData.userPaid) {
		    flash.pop({
			title : '',
			body : "Please include one more person other than the user paid.",
			type : 'alert-warning'
		    });
		    return;
		}
	    });
	}
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
