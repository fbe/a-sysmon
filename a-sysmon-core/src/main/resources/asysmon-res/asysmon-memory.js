var aSysMonApp = angular.module('ASysMonApp', []);

aSysMonApp.controller('ASysMonCtrl', function($scope, $http, $log) {
    //TODO zoom in / out on the time axis
    //TODO display committed and maximum memory
    //TODO get 'current' memory consumption in addition to GC requests

    $scope.showFullGcMarkers = true;
    $scope.$watch('showFullGcMarkers', function() {
        if($scope.gcs) {
            $scope.dataAsMap['_full_'].points.show = $scope.showFullGcMarkers;
            extractGcMarkings($scope.gcs);
            plot();
        }
    });

    $scope.showOtherGcMarkers = false;
    $scope.$watch('showOtherGcMarkers', function() {
        if($scope.gcs) {
            extractGcMarkings($scope.gcs);
            plot();
        }
    });

    $scope.showLegend = true;
    $scope.$watch('showLegend', function() {
        if($scope.gcs) {
            plot();
        }
    });

    function endMillis(gc) {
        return gc.durationNanos < 1000*1000 ? gc.startMillis : (gc.startMillis + gc.durationNanos / 1000 / 1000);
    }

    function isFullGc(gc) {
        return gc.type.indexOf(' major ') !== -1;
    }

    function extractGcMarkings(gcs) {
        $scope.gcMarkings = [];

        for(var i=0; i<gcs.length; i++) {
            var gc = gcs[i];
            if(isFullGc(gc) && $scope.showFullGcMarkers) {
                $scope.gcMarkings.push({color: 'rgba(0,0,100,.8)', lineWidth: 1, xaxis: {from: gc.startMillis, to: endMillis(gc)}});
            }
            if(! isFullGc(gc) && $scope.showOtherGcMarkers) {
                $scope.gcMarkings.push({color: 'rgba(50,50,150,.7)', lineWidth: 1, xaxis: {from: gc.startMillis, to: endMillis(gc)}});
            }
        }
    }

    function initFromResponse(data) {
        $scope.gcs = data.gcs;

        $scope.startTime = undefined;
        if(data.gcs && data.gcs.length) {
            $scope.startTime = new Date(data.gcs[0].startMillis);
        }
        $scope.endTime = new Date();
        $scope.dataSets = [];
        $scope.gcMarkings = [];

        function extractDataAsMap () {
            var result = {};

            for(var i=0; i<data.gcs.length; i++) {
                var gc = data.gcs[i];

                for(var memKind in gc.mem) {
                    if(!gc.mem.hasOwnProperty(memKind)) continue;
                    if(! result[memKind]) {
                        result[memKind] = {
                            label: memKind,
                            hoverable: false,
                            data: []
                        };
                    }
                    result[memKind].data.push([gc.startMillis, gc.mem[memKind].usedBefore / 1024 / 1024]);
                    result[memKind].data.push([gc.startMillis + (gc.durationNanos / 1000000), gc.mem[memKind].usedAfter / 1024 / 1024]);
                }
                if(!result['_full_']) {
                    result['_full_'] = {
                        label: '_full_',
                        data: [],
                        lines: {show: false},
                        color: 'rgb(0,0,100)',
                        points: {show: true, radius: 7, fill: true, fillColor: 'rgb(100, 100, 200)'}
                    }
                }
                if(isFullGc(gc)) {
                    result['_full_'].data.push([gc.startMillis, 0]);
                }
            }

            return result;
        }

        function sortedMemKinds(dataAsMap) {
            var result = [];
            for(var memKind in dataAsMap) {
                if(!dataAsMap.hasOwnProperty(memKind)) continue;
                result.push(memKind);
            }
            result.sort(function(a,b) {
                var aOrder = memKindSortOrder[a];
                var bOrder = memKindSortOrder[b];

                if(aOrder && bOrder) return aOrder - bOrder;
                if(!aOrder) return 1;
                if(!bOrder) return -1;
                return a < b ? -1 : 1;
            });

            return result;
        }

        if(data.gcs && data.gcs.length) {
            $scope.dataAsMap = extractDataAsMap();
            var memKinds = sortedMemKinds($scope.dataAsMap);

            $scope.dataSets = [];
            for(var i=0; i<memKinds.length; i++) {
                $scope.dataSets.push($scope.dataAsMap[memKinds[i]]);
            }

            extractGcMarkings(data.gcs);
        }
    }

    var memKindSortOrder = {
        'PS Perm Gen': 1,
        'PS Old Gen': 2,
        'PS Survivor Space': 3,
        'PS Eden Space': 4,
        '_full_': 10
    };

    function sendCommand(cmd) {
        $http.get(cmd).success(function(data) {
            initFromResponse(data);
            plot();
        });
    }

    $scope.refresh = function() {
        sendCommand('getData');
    };

    $scope.refresh();

//    $scope.plot = $scope.plotDummy;
    function plot() {
        $.plot(
            '#mem-gc-placeholder',
            $scope.dataSets,
            {
                legend: {
                    show: $scope.showLegend,
                    position: 'ne',
                    backgroundOpacity: .7,
                    labelFormatter: function(label) {return label.charAt(0) === '_' ? null : label;}
                },
                xaxis: {mode: 'time'},
                yaxis: {
                    axisLabel: 'Memory Size (MB)',
                    transform: function(v) {return v;}
                },
                series: {stack: true},
//                colors: [0, 1, 2, 3, 4, 5, 6],
                lines: {
                    lineWidth: 1,
                    fill: true
                },
                grid: {
                    hoverable: true,
                    mouseActiveRadius: 15,
//                    backgroundColor: { colors: ["#D1D1D1", "#7A7A7A"] },
                    markings: $scope.gcMarkings
                }
            }
        );
    }

    (function() {
        var previousPoint = null;

        function tooltipFor(item) {
            for(var i=0; i<$scope.gcs.length; i++) {
                var gc = $scope.gcs[i];
                if(gc.startMillis !== item.datapoint[0]) { //TODO is there a better way to do this?
                    continue;
                }
                //TODO layout of tool tip
                //TODO relative change of used memory per mem kind
                //TODO *committed* memory (+ info if changed)
                return 'cause: ' + gc.cause + '<br>' +
                    'type: ' + gc.type + '<br>' +
                    'algorithm: ' + gc.algorithm + '<br>' +
                    'duration: ' + gc.durationNanos + 'ns';
            }
            return '';
        }

        $('#mem-gc-placeholder').bind("plothover", function (event, pos, item) {
            if (item) {
                if (previousPoint != item.dataIndex) {
                    previousPoint = item.dataIndex;

                    $("#tooltip").remove();

                    var x = item.datapoint[0];
                    var y = item.datapoint[1];

//                    var text = 'moin moin: ' + item.series.label;

                    showTooltip(item.pageX, item.pageY, tooltipFor(item));
//                        months[x-  1] + "<br/>" + "<strong>" + y + "</strong> (" + item.series.label + ")");
                }
            }
            else {
                $("#tooltip").remove();
                previousPoint = null;
            }
        });

        //TODO nicer styling, move styling to CSS file
        function showTooltip(x, y, contents) {
            $('<div id="tooltip">' + contents + '</div>').css({
                position: 'absolute',
                display: 'none',
                top: y + 5,
                left: x + 20,
                border: '2px solid #4572A7',
                padding: '2px',
                size: '10',
                'background-color': '#fff',
                opacity: 0.90
            }).appendTo("body").fadeIn(200);
        }
    }());



//    var asdf = {
//        "datapoint":[1387572019779,628.5712509155273],
//        "dataIndex":1532,
//        "series":{
//            "points":{"show":false,"radius":3,"lineWidth":2,"fill":true,"fillColor":"#ffffff","symbol":"circle"},
//            "lines":{"lineWidth":2,"fill":false,"fillColor":null,"steps":false,"show":true,"zero":false},
//            "bars":{"show":false,"lineWidth":2,"barWidth":1,"fill":true,"fillColor":null,"align":"left","horizontal":false,"zero":true},
//            "shadowSize":3,
//            "highlightColor":null,
//            "stack":true,
//            "data": '...',
//            "label":"PS Eden Space",
//            "color":"rgb(77,167,77)",
//            "xaxis": '...',
//            "datapoints":'...'
//        },
//        "seriesIndex":3,
//        "pageX":1152,
//        "pageY":229};
});

