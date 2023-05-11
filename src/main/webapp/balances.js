var balanceChart;

function transparent(color, alpha) {
    var i = color.split(', ');
    i[i.length - 1] = alpha + ')'
    return i.join(', ');
};

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

function populateSummary(account, period) {
    var settings = {
      "url": "http://localhost:9999/graphqls",
      "method": "POST",
      "timeout": 0,
      "headers": {
        "Content-Type": "application/json"
      },
      "data": JSON.stringify({
        query: 'query {\r\n    balanceSummary(account: \"' + account + '\", period: ' + period + ') {\r\n        balance {\r\n            min\r\n            max\r\n            sum\r\n            median\r\n            average\r\n        }\r\n        income {\r\n            min\r\n            max\r\n            sum\r\n            median\r\n            average\r\n        }\r\n        outcome {\r\n            min\r\n            max\r\n            sum\r\n            median\r\n            average\r\n        }\r\n    }\r\n}',
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

function populateChart(account, period) {
    var settings = {
      "url": "http://localhost:9999/graphqls",
      "method": "POST",
      "timeout": 0,
      "headers": {
        "Content-Type": "application/json"
      },
      "data": JSON.stringify({
        query: 'query {\r\n    accountBalanceChart(account: \"' + account + '\", period: ' + period + ') {\r\n        ids        \r\n        title        \r\n        labels\r\n        datasets {\r\n            label\r\n            backgroundColor\r\n            data\r\n        }\r\n    }\r\n}',
        variables: {}
      })
    };

    $.ajax(settings).done(function (response) {
        if(balanceChart) balanceChart.destroy();
        var ctx = document.getElementById('account-balance-chart').getContext('2d');
        balanceChart = new Chart(ctx, {
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
              onClick: (e) => { chartClick(e, response.data.accountBalanceChart, "account-balance-table"); },
              plugins: {
                title: {
                  display: true,
                  text: response.data.accountBalanceChart.title
                },
              },
              responsive: true
          }});
    });
};

populateChart("c68f58bd-f17e-4878-afc6-afcf36ad99f1", "Week");
populateSummary("c68f58bd-f17e-4878-afc6-afcf36ad99f1", "Week");