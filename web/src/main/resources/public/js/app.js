var app = angular.module( 'DemoApp', ['ui.bootstrap'] );

app.controller('MainController', function($scope, $http) {

    $scope.parcels = [];
    $scope.selectedParcel = {};

    $scope.statusMapping = {
        PENDING : "Pending",
        DISPATCHED : "Dispatched",
        AT_DEPOT : "At Depot",
        OUT_FOR_DELIVERY : "Out for Delivery"
    };

    $scope.serverModes = [
        {
            name : "DIRECT",
            description : "Direct"
        },
        {
            name : "CONSUL",
            description : "Consul"
        },
        {
            name : "LINKERD",
            description : "Linkerd"
        }
    ];

    $scope.selectedServerMode = $scope.serverModes[0];

    $http.get('/api/settings')
        .then(function(response) {

            $("input:radio[name=inlineRadioOptions]").filter("[value='" + response.data.provider + "']").attr('checked', true);

        });

    $http.get('/api/parcels/')
        .then(function(response) {

            for(var i = 0; i < response.data.length; i++) {
                $scope.parcels.push(response.data[i]);
            }

            var slider = new Slider("#ex1");

            slider.on("change", function(data) {

                var newRate = data.newValue;

                if($scope.intervalId > -1) {
                    clearInterval($scope.intervalId);
                }

                if(newRate > 0) {
                    $scope.intervalId = setInterval($scope.parcelRequest, 1000 / newRate)
                }
            });
        });

    $scope.selectParcel = function(id) {

        $http.get('/api/parcels/' + id)
            .then(function(response) {
                $scope.selectedParcel = response.data;
                console.log(response);
            });

        console.log(id);
    };

    $scope.intervalId = -1;
    $scope.requestRoundTripTimeMs = 0;
    $scope.totalRequests = 0;

    $scope.parcelRequest = function() {
        var startTime = new Date().getTime();

        $http.get('/api/parcels/')
            .then(function(response) {

                var endTime = new Date().getTime();
                var roundTripTime = endTime - startTime;

                $scope.requestRoundTripTimeMs = roundTripTime;
                $scope.totalRequests++;
            });
    };

    $("input:radio[name=inlineRadioOptions]").click(function() {

        var val = {
            provider : $(this).val()
        };

        $http.post("/api/provider", val)
            .then(function(data) {
                console.log(data);
            });
    });

    $("#setServerMode").click(function() {
        $scope.setServerRemoteMode();
    });

    $scope.setServerRemoteMode = function() {
        var value = $("input:radio[name=inlineRadioOptions]").val();
        console.log('setting to ' + value);
    };


});
