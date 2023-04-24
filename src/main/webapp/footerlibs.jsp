<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.9.1/chart.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3" crossorigin="anonymous"></script>

<script>

    function populateSummary(account, period) {
        var settings = {
          "url": "http://localhost:9999/graphqls",
          "method": "POST",
          "timeout": 0,
          "headers": {
            "Content-Type": "application/json"
          },
          "data": JSON.stringify({
            query: "query {\r\n    balanceSummary(account: " + account + ", period: " + period + ") {\r\n        balance {\r\n            min\r\n            max\r\n            sum\r\n            median\r\n            average\r\n        }\r\n        income {\r\n            min\r\n            max\r\n            sum\r\n            median\r\n            average\r\n        }\r\n        outcome {\r\n            min\r\n            max\r\n            sum\r\n            median\r\n            average\r\n        }\r\n    }\r\n}",
            variables: {}
          })
        };

        $.ajax(settings).done(function (response) {
          $(".a-total-income").text(response.data.balanceSummary.income.sum);
          $(".a-total-outcome").text(response.data.balanceSummary.outcome.sum);
          $(".a-total-savings").text(response.data.balanceSummary.balance.sum);
          $(".a-total-income-min").text(response.data.balanceSummary.income.min);
          $(".a-total-outcome-min").text(response.data.balanceSummary.outcome.min);
          $(".a-total-savings-min").text(response.data.balanceSummary.balance.min);
          $(".a-total-income-max").text(response.data.balanceSummary.income.max);
          $(".a-total-outcome-max").text(response.data.balanceSummary.outcome.max);
          $(".a-total-savings-max").text(response.data.balanceSummary.balance.max);
          $(".a-total-income-median").text(response.data.balanceSummary.income.median);
          $(".a-total-outcome-median").text(response.data.balanceSummary.outcome.median);
          $(".a-total-savings-median").text(response.data.balanceSummary.balance.median);
          $(".a-total-income-average").text(response.data.balanceSummary.income.average);
          $(".a-total-outcome-average").text(response.data.balanceSummary.outcome.average);
          $(".a-total-savings-average").text(response.data.balanceSummary.balance.average);
        });
    };

</script>

<script>

    var watrCharts = [];

    function populateTable(item, tableelement) {
        var settings = {
          "url": "http://localhost:9999/graphqls",
          "method": "POST",
          "timeout": 0,
          "headers": {
            "Content-Type": "application/json"
          },
          "data": JSON.stringify({
            query: 'query {\r\n    balanceById(id: \"' + item + '\") {\r\n        income\r\n        outcome\r\n        start\r\n        end\r\n        period\r\n        transactions {\r\n            item\r\n            date\r\n            amount\r\n        }\r\n    }\r\n}',
            variables: {}
          })
        };

        $.ajax(settings).done(function (response) {
          $(".a-table-income").text(response.data.balanceById.income);
          $(".a-table-outcome").text(response.data.balanceById.outcome);
          $("#" + tableelement + " tr").remove();
          const table = document.getElementById(tableelement);
          response.data.balanceById.transactions.forEach(item => {
              let row = table.insertRow();
              let itemtext = row.insertCell(0);
              itemtext.innerHTML = item.item;
              let date = row.insertCell(1);
              date.innerHTML = item.date;
              let amount = row.insertCell(2);
              amount.innerHTML = item.amount;
          })
        });
    };

    function chartClick(e, accountBalanceChart, accountBalanceTable) {
        const points = e.chart.getElementsAtEventForMode(e, 'nearest', { intersect: true }, true)
        if(points && points.length > 0) {
            populateTable(accountBalanceChart.ids[points[0].index], accountBalanceTable);
        }
    };

    function populateChart(account, period, index, chartelement, tableelement) {
        var settings = {
          "url": "http://localhost:9999/graphqls",
          "method": "POST",
          "timeout": 0,
          "headers": {
            "Content-Type": "application/json"
          },
          "data": JSON.stringify({
            query: "query {\r\n    accountBalanceChart(account: " + account + ", period: " + period + ") {\r\n        ids        \r\n        title        \r\n        labels\r\n        datasets {\r\n            label\r\n            backgroundColor\r\n            data\r\n        }\r\n    }\r\n}",
            variables: {}
          })
        };

        $.ajax(settings).done(function (response) {
            if(watrCharts[index]) watrCharts[index].destroy();
            var ctx = document.getElementById(chartelement).getContext('2d');
            watrCharts[index] = new Chart(ctx, {
                type: 'line',
                data: {
                  labels: response.data.accountBalanceChart.labels,
                  datasets: [
                  {
                    label: response.data.accountBalanceChart.datasets[0].label,
                    borderColor: response.data.accountBalanceChart.datasets[0].backgroundColor,
                    backgroundColor: transparent(response.data.accountBalanceChart.datasets[0].backgroundColor, 0.5),
                    fill: true,
                    tension: 0.4,
                    data: response.data.accountBalanceChart.datasets[0].data,
                    pointRadius: 0,
                    pointHitRadius: 5,
                  }, {
                     label: response.data.accountBalanceChart.datasets[1].label,
                     borderColor: response.data.accountBalanceChart.datasets[1].backgroundColor,
                     backgroundColor: transparent(response.data.accountBalanceChart.datasets[1].backgroundColor, 0.5),
                     fill: true,
                     tension: 0.4,
                     data: response.data.accountBalanceChart.datasets[1].data,
                    pointRadius: 0,
                    pointHitRadius: 5,
                   }, {
                   label: response.data.accountBalanceChart.datasets[2].label,
                   borderColor: response.data.accountBalanceChart.datasets[2].backgroundColor,
                   backgroundColor: transparent(response.data.accountBalanceChart.datasets[2].backgroundColor, 0.5),
                   fill: true,
                   tension: 0.4,
                   data: response.data.accountBalanceChart.datasets[2].data,
                    pointRadius: 0,
                    pointHitRadius: 5,
                 }, {
                    label: response.data.accountBalanceChart.datasets[3].label,
                    borderColor: response.data.accountBalanceChart.datasets[3].backgroundColor,
                    backgroundColor: transparent(response.data.accountBalanceChart.datasets[3].backgroundColor, 0.5),
                    fill: true,
                    tension: 0.4,
                    data: response.data.accountBalanceChart.datasets[3].data,
                     pointRadius: 0,
                     pointHitRadius: 5,
                  }],
                },
              options: {
                  onClick: (e) => { chartClick(e, response.data.accountBalanceChart, tableelement); },
                  plugins: {
                    title: {
                      display: true,
                      text: response.data.accountBalanceChart.title
                    },
                  },
                  responsive: true
              }});
        });
    }

    function transparent(color, alpha) {
        var teta = color.split(', ');
        teta[teta.length - 1] = alpha + ')'
        return teta.join(', ');
    };

    $('#myDropdown li').on('click', function(){
        populateSummary("\"c68f58bd-f17e-4878-afc6-afcf36ad99f1\"", $(this).text());
        populateChart("\"c68f58bd-f17e-4878-afc6-afcf36ad99f1\"", $(this).text(), 0, "transactions-account-a", "account-a-table");
    });

    populateSummary("\"c68f58bd-f17e-4878-afc6-afcf36ad99f1\"", "Week");
    populateChart("\"c68f58bd-f17e-4878-afc6-afcf36ad99f1\"", "Week", 0, "transactions-account-a", "account-a-table");

</script>

<!--
<script>


var settings29 = {
  "url": "http://localhost:9999/graphqls",
  "method": "POST",
  "timeout": 0,
  "headers": {
    "Content-Type": "application/json"
  },
  "data": JSON.stringify({
    query: "query {\r\n    balancesByPeriod(start: \"01/11/2022\", end: \"31/11/2022\") {\r\n        period\r\n        start        \r\n        end\r\n        income\r\n        outcome\r\n        current\r\n    }\r\n}",
    variables: {}
  })
};

$.ajax(settings29).done(function (response) {
  $(".cmp_current-balance").text(response.data.balancesByPeriod[0].current);
  $(".cmp_current-earnings").text(response.data.balancesByPeriod[0].income);
  $(".cmp_current-expenses").text(response.data.balancesByPeriod[0].outcome);
});


var settings30 = {
  "url": "http://localhost:9999/graphqls",
  "method": "POST",
  "timeout": 0,
  "headers": {
    "Content-Type": "application/json"
  },
  "data": JSON.stringify({
    query: "query {\r\n    balanceSummary {\r\n        min\r\n        max\r\n        sum\r\n        median\r\n        average\r\n    }\r\n}",
    variables: {}
  })
};

$.ajax(settings30).done(function (response) {
  $(".cmp_total_earnings").text(response.data.balanceSummary.sum + " EUR");
  $(".cmp_median-balance").text(response.data.balanceSummary.median + " EUR");
  $(".cmp_average-balance").text(response.data.balanceSummary.average + " EUR");
});

var settings5 = {
  "url": "http://localhost:9999/graphqls",
  "method": "POST",
  "timeout": 0,
  "headers": {
    "Content-Type": "application/json"
  },
  "data": JSON.stringify({
    query: "query {\r\n    chartBarByWeekByItem {\r\n        title        \r\n        labels\r\n        datasets {\r\n            label\r\n            backgroundColor\r\n            data\r\n        }\r\n    }\r\n}",
    variables: {}
  })
};

$.ajax(settings5).done(function (response) {
            var ctx5 = document.getElementById("masbarchart").getContext('2d');
            var myChart5 = new Chart(ctx5, {
              type: 'bar',
              data: {
                labels: response.data.chartBarByWeekByItem.labels,
                datasets: response.data.chartBarByWeekByItem.datasets,
              },
            options: {
              plugins: {
                title: {
                  display: true,
                  text: response.data.chartBarByWeekByItem.title
                },
              },
              responsive: true
            }});
});

    var settings4 = {
      "url": "http://localhost:9999/graphqls",
      "method": "POST",
      "timeout": 0,
      "headers": {
        "Content-Type": "application/json"
      },
      "data": JSON.stringify({
        query: "query {\r\n    chartExpensesByPeriodByItem {\r\n        title        \r\n        labels\r\n        datasets {\r\n            label\r\n            backgroundColor\r\n            data\r\n        }\r\n    }\r\n}",
        variables: {}
      })
    };

    $.ajax(settings4).done(function (response) {
        var ctx4 = document.getElementById("piechart").getContext('2d');
        var myChart4 = new Chart(ctx4, {
          type: 'pie',
          data: {
            labels: response.data.chartExpensesByPeriodByItem.labels,
            datasets: response.data.chartExpensesByPeriodByItem.datasets,
          },
        options: {
            plugins: {
              title: {
                display: true,
                text: response.data.chartExpensesByPeriodByItem.title
              },
            },
            responsive: true
        }});
    });


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
              maintainAspectRatio: false,
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

    var settings25 = {
      "url": "http://localhost:9999/graphqls",
      "method": "POST",
      "timeout": 0,
      "headers": {
        "Content-Type": "application/json"
      },
      "data": JSON.stringify({
        query: "query {\r\n    chartBarBalance {\r\n        title        \r\n        labels\r\n        datasets {\r\n            label\r\n            backgroundColor            \r\n            data\r\n        }\r\n    }\r\n}",
        variables: {}
      })
    };

    $.ajax(settings25).done(function (response) {
      var ctx25 = document.getElementById("tetachart").getContext('2d');
                var myChart25 = new Chart(ctx25, {
                  type: 'bar',
                  data: {
                    labels: response.data.chartBarBalance.labels,
                    datasets: response.data.chartBarBalance.datasets,
                  },
options: {
    responsive: true,
    plugins: {
      title: {
        display: true,
        text: response.data.chartBarBalance.title
      }
    }
  },
          });


          var ctx26 = document.getElementById("culochart").getContext('2d');
                    var myChart26 = new Chart(ctx26, {
                      type: 'line',
                      data: {
                        labels: response.data.chartBarBalance.labels,
                        datasets: [
                        {
                          label: response.data.chartBarBalance.datasets[2].label,
                          borderColor: response.data.chartBarBalance.datasets[2].backgroundColor,
                          backgroundColor: transparent(response.data.chartBarBalance.datasets[2].backgroundColor, 0.5),
                          fill: true,
                          tension: 0.4,
                          data: response.data.chartBarBalance.datasets[2].data,
                          pointRadius: 0,
                          pointHitRadius: 5,
                        }, {
                           label: response.data.chartBarBalance.datasets[1].label,
                           borderColor: response.data.chartBarBalance.datasets[1].backgroundColor,
                           backgroundColor: transparent(response.data.chartBarBalance.datasets[1].backgroundColor, 0.5),
                           fill: true,
                           tension: 0.4,
                           data: response.data.chartBarBalance.datasets[1].data,
                          pointRadius: 0,
                          pointHitRadius: 5,
                         }, {
                         label: response.data.chartBarBalance.datasets[0].label,
                         borderColor: response.data.chartBarBalance.datasets[0].backgroundColor,
                         backgroundColor: transparent(response.data.chartBarBalance.datasets[0].backgroundColor, 0.5),
                         fill: true,
                         tension: 0.4,
                         data: response.data.chartBarBalance.datasets[0].data,
                          pointRadius: 0,
                          pointHitRadius: 5,
                       }],
                      },
                    options: {
                        plugins: {
                          title: {
                            display: true,
                            text: response.data.chartBarBalance.title
                          },
                        },
                        responsive: true
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
        query: "query {\r\n    chartBalance {\r\n        title        \r\n        labels\r\n        datasets {\r\n            label\r\n            backgroundColor\r\n            data\r\n        }\r\n    }\r\n}",
        variables: {}
      })
    };

    $.ajax(settings).done(function (response) {
      var ctx = document.getElementById("chart").getContext('2d');
          var myChart = new Chart(ctx, {
            type: 'bar',
            data: {
              labels: response.data.chartBalance.labels,
              datasets: [{
                label: response.data.chartBalance.datasets[1].label,
                backgroundColor: response.data.chartBalance.datasets[1].backgroundColor,
                data: response.data.chartBalance.datasets[1].data,
              }, {
                label: response.data.chartBalance.datasets[2].label,
                backgroundColor: response.data.chartBalance.datasets[2].backgroundColor,
                data: response.data.chartBalance.datasets[2].data,
              }],
            },
          options: {
              plugins: {
                title: {
                  display: true,
                  text: response.data.chartBalance.title
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
              labels: response.data.chartBalance.labels,
              datasets: [
              {
                label: response.data.chartBalance.datasets[1].label,
                borderColor: response.data.chartBalance.datasets[1].backgroundColor,
                backgroundColor: transparent(response.data.chartBalance.datasets[1].backgroundColor, 0.5),
                fill: true,
                tension: 0.4,
                data: response.data.chartBalance.datasets[1].data,
                pointRadius: 0,
                pointHitRadius: 5,
              }, {
                 label: response.data.chartBalance.datasets[0].label,
                 borderColor: response.data.chartBalance.datasets[0].backgroundColor,
                 backgroundColor: transparent(response.data.chartBalance.datasets[0].backgroundColor, 0.5),
                 fill: true,
                 tension: 0.4,
                 data: response.data.chartBalance.datasets[0].data,
                pointRadius: 0,
                pointHitRadius: 5,
               }, {
               label: response.data.chartBalance.datasets[2].label,
               borderColor: response.data.chartBalance.datasets[2].backgroundColor,
               backgroundColor: transparent(response.data.chartBalance.datasets[2].backgroundColor, 0.5),
               fill: true,
               tension: 0.4,
               data: response.data.chartBalance.datasets[2].data,
                pointRadius: 0,
                pointHitRadius: 5,
             }],
            },
          options: {
              plugins: {
                title: {
                  display: true,
                  text: response.data.chartBalance.title
                },
              },
              responsive: true
          }});
    });
</script>

-->