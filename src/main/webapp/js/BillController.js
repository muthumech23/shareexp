'use strict'
var billControllers = angular.module('BillControllers', []);

billControllers.controller('BillListController', function($scope, homeBillData, $state) {
    $scope.usersBillData = homeBillData;

    $scope.userBill = function(userId) {
	$scope.selectUser(userId);
	$state.go('billhome.userlist', {
	    userId : userId
	});
    };

});

billControllers.controller('BillUserController', function($scope, $state, $stateParams, BillingServices, UserServices, cfpLoadingBar, flash) {

    $scope.editBill = function(billId) {
	$state.go('billhome.edit', {
	    billId : billId
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
    $scope.bills = recentBills;

    $scope.editBill = function(billId) {
	$state.go('billhome.edit', {
	    billId : billId
	});
    };

});

billControllers.controller('BillAddController', function($scope, $state, cfpLoadingBar, flash, BillingServices, addBill, SessionService, $filter) {

    $scope.open = function($event) {
	$event.preventDefault();
	$event.stopPropagation();

	$scope.opened = true;
    };

    $scope.addBillData = addBill;

    $scope.bill = {};

    $scope.addBillSplits = addBill.billSplits;

    $scope.updatedBillSPlitList = [];

    $scope.bill.splitType = 'equally';

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
			body : 'Please include one more person other than the user paid.',
			type : 'alert-warning'
		    });
		    return;
		}
	    });
	}

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
		    console.log('billsplit.userId 1' + billsplit.userId);
		    flash.pop({
			title : '',
			body : 'Please include one more person other than the user paid.',
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
			body : 'Please include one more person other than the user paid.',
			type : 'alert-warning'
		    });
		    return;
		}
	    });
	}

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
