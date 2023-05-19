<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.9.1/chart.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3" crossorigin="anonymous"></script>

<script src="balances.js"></script>
<script>

    var itemChart;

    function populateByItemChart(itemid, chartelement) {
        var settings = {
          "url": "http://localhost:9999/graphqls",
          "method": "POST",
          "timeout": 0,
          "headers": {
            "Content-Type": "application/json"
          },
          "data": JSON.stringify({
            query: 'query {\r\n    accountByItemChart(item: \"' + itemid + '\") {\r\n        title\r\n        labels\r\n        datasets {\r\n            label\r\n            backgroundColor\r\n            data\r\n        }\r\n    }\r\n}',
            variables: {}
          })
        };

        $.ajax(settings).done(function (response) {
            if(itemChart) itemChart.destroy();
            var ctx = document.getElementById(chartelement).getContext('2d');
            itemChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: response.data.accountByItemChart.labels,
                    datasets: response.data.accountByItemChart.datasets,
                },
                options: {
                    plugins: {
                        title: {
                            display: true,
                            text: response.data.accountByItemChart.title
                        },
                    },
                responsive: true
            }});
        });
    };

    var textChart;

    function populateByTextChart(account, text) {
        var settings = {
          "url": "http://localhost:9999/graphqls",
          "method": "POST",
          "timeout": 0,
          "headers": {
            "Content-Type": "application/json"
          },
          "data": JSON.stringify({
            query: 'query {\r\n    accountByTextChart(account: \"c68f58bd-f17e-4878-afc6-afcf36ad99f1\", text: \"' + text + '\") {\r\n        title\r\n        labels\r\n        datasets {\r\n            label\r\n            backgroundColor\r\n            data\r\n        }\r\n    }\r\n}',
            variables: {}
          })
        };

        $.ajax(settings).done(function (response) {
            if(textChart) textChart.destroy();
            var ctx = document.getElementById("byitems-account-a-text").getContext('2d');
            textChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: response.data.accountByTextChart.labels,
                    datasets: response.data.accountByTextChart.datasets,
                },
                options: {
                    plugins: {
                        title: {
                            display: true,
                            text: response.data.accountByTextChart.title
                        },
                    },
                responsive: true
            }});
        });
    };

    function populateByItemTable(itemid, tableelement) {
        var settings = {
          "url": "http://localhost:9999/graphqls",
          "method": "POST",
          "timeout": 0,
          "headers": {
            "Content-Type": "application/json"
          },
          "data": JSON.stringify({
            query: 'query {\r\n    transactionsByItem(item: \"' + itemid + '\") {\r\n        id\r\n        item\r\n        amount\r\n        transactions {\r\n            item\r\n            date\r\n            amount\r\n        }\r\n    }\r\n}',
            variables: {}
          })
        };

        $.ajax(settings).done(function (response) {
            $("#" + tableelement + " tr").remove();
            const table = document.getElementById(tableelement);
            const map1 = new Map();
            response.data.transactionsByItem.transactions.forEach(item => {
                let row = table.insertRow();
                let itemtext = row.insertCell(0);
                itemtext.innerHTML = item.item;
                let date = row.insertCell(1);
                date.innerHTML = item.date;
                let amount = row.insertCell(2);
                amount.innerHTML = item.amount;

                let popo = item.amount;
                if(map1.has(item.item)) {
                    popo += map1.get(item.item);
                }
                map1.set(item.item, popo);
            });

            $("#byitems-account-a-table-grouped tr").remove();
            const table2 = document.getElementById("byitems-account-a-table-grouped");
            for (let [key, value] of map1) {
                let row2 = table2.insertRow();
                let itemtext2 = row2.insertCell(0);
                itemtext2.innerHTML = key;
                let amount2 = row2.insertCell(1);
                amount2.innerHTML = value;
                row2.setAttribute('onclick', 'populateByTextChart("' + itemid + '", this.cells[0].innerHTML)');
            }
        });
    };

    function populateByItemSummary(itemid) {
        var settings = {
          "url": "http://localhost:9999/graphqls",
          "method": "POST",
          "timeout": 0,
          "headers": {
            "Content-Type": "application/json"
          },
          "data": JSON.stringify({
            query: 'query {\r\n    balanceByItem(item: \"' + itemid + '\") {\r\n        min\r\n        max\r\n        sum\r\n        median\r\n        average\r\n    }\r\n}',
            variables: {}
          })
        };

        $.ajax(settings).done(function (response) {
            $(".a-item-total").text(response.data.balanceByItem.sum);
            $(".a-item-average").text(response.data.balanceByItem.average);
            $(".a-item-median").text(response.data.balanceByItem.median);
            $(".a-item-min").text(response.data.balanceByItem.min);
            $(".a-item-max").text(response.data.balanceByItem.max);
        });
    };

    function chartByItemsClick(e, chart, table) {
        const points = e.chart.getElementsAtEventForMode(e, 'nearest', { intersect: true }, true)
        if(points && points.length > 0) {
            populateByItemChart(chart.ids[points[0].index], "byitems-account-a-item");
            populateByItemTable(chart.ids[points[0].index], table);
            populateByItemSummary(chart.ids[points[0].index]);
        }
    };

    function populateByItemsChart(account, type, chartelement, tableelement) {
        var settings = {
          "url": "http://localhost:9999/graphqls",
          "method": "POST",
          "timeout": 0,
          "headers": {
            "Content-Type": "application/json"
          },
          "data": JSON.stringify({
            query: "query {\r\n    accountByItemsChart(account: " + account + ", type: " + type + ") {\r\n        ids\r\n        title\r\n        labels\r\n        datasets {\r\n            label\r\n            backgroundColor\r\n            data\r\n        }\r\n    }\r\n}",
            variables: {}
          })
        };

        $.ajax(settings).done(function (response) {
            var ctx = document.getElementById(chartelement).getContext('2d');
            var myChart = new Chart(ctx, {
              type: 'bar',
              data: {
                labels: response.data.accountByItemsChart.labels,
                datasets: response.data.accountByItemsChart.datasets,
              },
            options: {
              indexAxis: 'y',
              legend: {
                position: 'left',
              },
              onClick: (e) => { chartByItemsClick(e, response.data.accountByItemsChart, tableelement); },
              plugins: {
                title: {
                  display: true,
                  text: response.data.accountByItemsChart.title
                },
              },
              responsive: true
            }});
        });
    }
    populateByItemsChart("\"c68f58bd-f17e-4878-afc6-afcf36ad99f1\"", "All", "byitems-account-a", "byitems-account-a-table");
</script>
<!--
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
-->