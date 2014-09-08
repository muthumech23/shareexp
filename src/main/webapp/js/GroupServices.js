var groupServices = angular.module('GroupServices', []);

groupServices.factory('GroupServices', function($resource, FlashService, SessionService) {

    var groupRes = $resource('api/group/:Id', {groupId: '@groupid'});
    var userId = SessionService.get('userId');

    return {
        getGroups: function() {

            var listGroup = $resource('api/group/list/:Id');

            var groups = listGroup.query({Id: userId}).$promise;
            groups.then(function(response) {
                return response.data;
            }, function(response) {
                FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
            });
            return groups;
        },
        addGroup: function(sharegroup) {
            var group = groupRes.save(sharegroup).$promise;
            return group;
        },
        getGroup: function(groupId) {
            var editGroup = groupRes.get({Id: groupId}).$promise;
            return editGroup;
        },
        getGroupBills: function(groupId) {
            
            var groupBillRes = $resource('api/bill/group/:Id', {}, {getBills: {method: 'GET', isArray: true}});
            
            var bills = groupBillRes.getBills({Id: groupId}).$promise;

            return bills;
        }
    };
});

