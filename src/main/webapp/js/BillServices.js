'use strict'
var billServices = angular.module('BillServices', []);

billServices.factory('BillingServices', function($resource, flash, SessionService) {

    var billAllRes = $resource('api/bill/total/:userId', {}, {
	getUsersBillData : {
	    method : 'GET',
	    isArray : true
	}
    });
    var billRecentRes = $resource('api/bill/recent/:Id', {}, {
	getRecentBills : {
	    method : 'GET',
	    isArray : true
	}
    });
    var billUserRecentRes = $resource('api/bill/recentuser/:Id', {}, {
	getUserRecentBills : {
	    method : 'POST',
	    isArray : true
	}
    });
    var addBillRes = $resource('api/bill/add/:Id', {}, {
	addBill : {
	    method : 'GET'
	}
    });
    var billingRes = $resource('api/bill/:Id', {
	billId : '@billid'
    }, {
	update : {
	    method : 'PUT'
	}
    });
    var userId = SessionService.get('userId');

    return {
	getUsersBill : function() {

	    var billData = billAllRes.getUsersBillData({
		userId : userId
	    }).$promise;

	    billData.then(function(response) {
		return response.data;
	    }, function(response) {
		$scope.errorresource = response.data;
		flash.pop({title: '', body: $scope.errorresource.code + ": " + $scope.errorresource.message, type: 'alert-danger'});
	    });
	    return billData;
	},
	getBillResource : function(billData) {
	    var addBill = billingRes.save(billData).$promise;
	    return addBill;
	},
	showBillResource : function(billId) {
	    var showBill = billingRes.get({
		Id : billId
	    }).$promise;
	    return showBill;
	},
	addBillPage : function() {

	    var addBill = addBillRes.addBill({
		Id : userId
	    }).$promise;

	    addBill.then(function(response) {
		return response.data;
	    }, function(response) {
		$scope.errorresource = response.data;
		flash.pop({title: '', body: $scope.errorresource.code + ": " + $scope.errorresource.message, type: 'alert-danger'});
	    });
	    return addBill;
	},
	getBills : function() {

	    var bills = billRecentRes.getRecentBills({
		Id : userId
	    }).$promise;

	    bills.then(function(response) {
		return response.data;
	    }, function(response) {
		$scope.errorresource = response.data;
		flash.pop({title: '', body: $scope.errorresource.code + ": " + $scope.errorresource.message, type: 'alert-danger'});
	    });
	    return bills;
	},
	getUserBills : function(id) {
	    var bills = billUserRecentRes.getUserRecentBills({
		Id : id
	    }, userId).$promise;

	    bills.then(function(response) {
		return response.data;
	    }, function(response) {
		$scope.errorresource = response.data;
		flash.pop({title: '', body: $scope.errorresource.code + ": " + $scope.errorresource.message, type: 'alert-danger'});
	    });
	    return bills;
	}
    };
});
