<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.9.1/chart.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3" crossorigin="anonymous"></script>


<script>

    var settings2 = {
      "url": "http://localhost:9999/graphqls",
      "method": "POST",
      "timeout": 0,
      "headers": {
        "Content-Type": "application/json"
      },
      "data": JSON.stringify({
        query: "query {\r\n    chartBarByPeriodByItem {\r\n        title        \r\n        labels\r\n        datasets {\r\n            label\r\n            backgroundColor\r\n            data\r\n        }\r\n    }\r\n}",
        variables: {}
      })
    };

    $.ajax(settings2).done(function (response) {
          var ctx3 = document.getElementById("barchart").getContext('2d');
          var myChart3 = new Chart(ctx3, {
            type: 'bar',
            data: {
              labels: response.data.chartBarByPeriodByItem.labels,
              datasets: response.data.chartBarByPeriodByItem.datasets,
            },
          options: {
              plugins: {
                title: {
                  display: true,
                  text: response.data.chartBarByPeriodByItem.title
                },
              },
              responsive: true,
              scales: {
                x: {
                  stacked: true,
                },
                y: {
                  stacked: true
                }
              }
          }});
    });


    var settings = {
      "url": "http://localhost:9999/graphqls",
      "method": "POST",
      "timeout": 0,
      "headers": {
        "Content-Type": "application/json"
      },
      "data": JSON.stringify({
        query: "query {\r\n    chartLineByPeriod {\r\n        title        \r\n        labels\r\n        datasets {\r\n            label\r\n            data\r\n        }\r\n    }\r\n}",
        variables: {}
      })
    };

    $.ajax(settings).done(function (response) {
      var ctx = document.getElementById("chart").getContext('2d');
          var myChart = new Chart(ctx, {
            type: 'bar',
            data: {
              labels: response.data.chartLineByPeriod.labels,
              datasets: [{
                label: response.data.chartLineByPeriod.datasets[0].label,
                backgroundColor: "#caf270",
                data: response.data.chartLineByPeriod.datasets[0].data,
              }, {
                label: response.data.chartLineByPeriod.datasets[1].label,
                backgroundColor: "#45c490",
                data: response.data.chartLineByPeriod.datasets[1].data,
              }],
            },
          options: {
              plugins: {
                title: {
                  display: true,
                  text: response.data.chartLineByPeriod.title
                },
              },
              responsive: true,
              scales: {
                x: {
                  stacked: true,
                },
                y: {
                  stacked: true
                }
              }
          }});


         var ctx2 = document.getElementById("linechart").getContext('2d');
          var myChart2 = new Chart(ctx2, {
            type: 'line',
            data: {
              labels: response.data.chartLineByPeriod.labels,
              datasets: [{
                label: response.data.chartLineByPeriod.datasets[0].label,
                borderColor: "#caf270",
                fill: false,
                tension: 0.1,
                data: response.data.chartLineByPeriod.datasets[0].data,
              }, {
                label: response.data.chartLineByPeriod.datasets[1].label,
                backgroundColor: "#45c490",
                fill: false,
                tension: 0.1,
                data: response.data.chartLineByPeriod.datasets[1].data,
              }, {
               label: response.data.chartLineByPeriod.datasets[2].label,
               backgroundColor: "#672C5B",
               fill: false,
               tension: 0.1,
               data: response.data.chartLineByPeriod.datasets[2].data,
             }],
            },
          options: {
              plugins: {
                title: {
                  display: true,
                  text: response.data.chartLineByPeriod.title
                },
              },
              responsive: true
          }});
    });
</script>