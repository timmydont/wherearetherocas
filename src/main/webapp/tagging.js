function populateTaggingTable() {
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
      });
      location.hash = "";
      location.hash = "#account-balance-table-container";
    });
};