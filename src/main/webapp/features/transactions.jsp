<div class="transactions">
    <div class="row">
        <div id="myDropdown" class="dropdown">
          <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
            Period of Time
          </button>
          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="#">Day</a></li>
            <li><a class="dropdown-item" href="#">Week</a></li>
            <li><a class="dropdown-item" href="#">Month</a></li>
          </ul>
        </div>
    </div>
    <div class="row">
        <div class="row transactions-account-a-summary" style="margin-bottom: 10px;margin-top: 10px;">
            <div class="col-sm-4">
                <div class="card">
                <div class="card-body">
                    <div class="row">
                        <div class="col">
                        <h5 class="card-title">Income</h5>
                        <p class="card-text">by day 10/09/2023</p>
                        </div>
                        <div class="col">
                        <p class="a-total-income fw-bold fs-1"></p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">
                        <b>Median: </b><span class="a-total-income-median"></span>
                        <br>
                        <b>Average: </b><span class="a-total-income-average"></span>
                        </div>
                        <div class="col">
                        <b>Max: </b><span class="a-total-income-max"></span>
                        <br>
                        <b>Min: </b><span class="a-total-income-min"></span>
                        </div>
                    </div>
                </div>
                </div>
            </div>

            <div class="col-sm-4">
                <div class="card">
                <div class="card-body">
                    <div class="row">
                        <div class="col">
                        <h5 class="card-title">Outcome</h5>
                        <p class="card-text">by day 10/09/2023</p>
                        </div>
                        <div class="col">
                        <p class="a-total-outcome fw-bold fs-1"></p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">
                        <b>Median: </b><span class="a-total-outcome-median"></span>
                        <br>
                        <b>Average: </b><span class="a-total-outcome-average"></span>
                        </div>
                        <div class="col">
                        <b>Max: </b><span class="a-total-outcome-max"></span>
                        <br>
                        <b>Min: </b><span class="a-total-outcome-min"></span>
                        </div>
                    </div>
                </div>
                </div>
            </div>

            <div class="col-sm-4">
                <div class="card">
                <div class="card-body">
                    <div class="row">
                        <div class="col">
                        <h5 class="card-title">Savings</h5>
                        <p class="card-text">by day 10/09/2023</p>
                        </div>
                        <div class="col">
                        <p class="a-total-savings fw-bold fs-1"></p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">
                        <b>Median: </b><span class="a-total-savings-median"></span>
                        <br>
                        <b>Average: </b><span class="a-total-savings-average"></span>
                        </div>
                        <div class="col">
                        <b>Max: </b><span class="a-total-savings-max"></span>
                        <br>
                        <b>Min: </b><span class="a-total-savings-min"></span>
                        </div>
                    </div>
                </div>
                </div>
          </div>

        </div>

        <canvas id="transactions-account-a"></canvas>
        <div class="transactions-account-a-table" style="margin-top: 10px;">
            <p><b>Income: </b><span class="a-table-income">123</span><b> Outcome: </b><span class="a-table-outcome">123</span></p>

            <table id="account-a-table" class="table table-borderless table-striped table-earning">
              <thead>
                <tr>
                  <th>Item</th>
                  <th>Date</th>
                  <th>Amount</th>
                </tr>
              </thead>
              <tbody></tbody>
            </table>
        </div>
    </div>
</div>
