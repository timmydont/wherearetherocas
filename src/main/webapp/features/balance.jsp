<div class="row">
    <div class="col-3">
        <div class="form-floating">
          <select class="form-select" id="balancePeriodSelect" aria-label="Select the period of time...">
            <option selected>Open this select menu</option>
            <option value="1">Month</option>
            <option value="2">Week</option>
            <option value="3">Day</option>
          </select>
          <label for="balancePeriodSelect">Select the period of time...</label>
        </div>
    </div>

    <div class="col-3">
        <div class="input-group mb-3">
           <div class="form-floating">
              <input id="floatingInputSearchText" type="text" class="form-control" placeholder="Username" aria-label="Username">
              <label for="floatingInputSearchText">Start date</label>
          </div>
          <span class="input-group-text">@</span>
           <div class="form-floating">
              <input id="floatingInputSearchText2" type="text" class="form-control" placeholder="Username" aria-label="Username">
              <label for="floatingInputSearchText2">End date</label>
          </div>
        </div>
    </div>

    <div class="col-6">
        <div class="input-group mb-3">
          <div class="form-floating">
              <input id="floatingInputSearch" type="text" class="form-control" placeholder="Search for text" aria-label="Search for text" aria-describedby="button-addon2">
              <label for="floatingInputSearch">Search for text</label>
          </div>
          <button class="btn btn-outline-secondary" type="button" id="button-addon2">Search</button>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-4">
        <div class="card">
            <div class="card-body">
                <div class="row">
                    <div class="col">
                    <h5 class="card-title">Income</h5>
                    <p class="card-text">by day 10/09/2023</p>
                    </div>
                    <div class="col">
                    <p class="a-total-income fw-bold fs-2"></p>
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
    <div class="col-4">
        <div class="card">
            <div class="card-body">
                <div class="row">
                    <div class="col">
                    <h5 class="card-title">Outcome</h5>
                    <p class="card-text">by day 10/09/2023</p>
                    </div>
                    <div class="col">
                    <p class="a-total-outcome fw-bold fs-2"></p>
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
    <div class="col-4">
        <div class="card">
            <div class="card-body">
                <div class="row">
                    <div class="col">
                    <h5 class="card-title">Savings</h5>
                    <p class="card-text">by day 10/09/2023</p>
                    </div>
                    <div class="col">
                    <p class="a-total-savings fw-bold fs-2"></p>
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

<div class="row" style="margin-top: 10px;margin-bottom: 10px;">
    <canvas id="account-balance-chart"></canvas>
</div>

<div class="row">
    <div class="col">
        <canvas id="account-balance-income-chart"></canvas>
    </div>
    <div class="col">
        <canvas id="account-balance-outcome-chart"></canvas>
    </div>
</div>

<div class="row">
    <div class="col">
        <canvas id="account-balance-earning-chart"></canvas>
    </div>
    <div class="col">
        <canvas id="account-balance-savings-chart"></canvas>
    </div>
</div>

<div class="row" id="account-balance-table-container">
    <div class="col-9">
        <p>Transactions for <span class="period-span">Week</span> <span class="date-span">21/02/2022</span></p>
        <div class="account-balance-table">
            <table class="table table-borderless table-striped table-earning">
              <thead>
                <tr>
                  <th>Item</th>
                  <th>Date</th>
                  <th>Amount</th>
                </tr>
              </thead>
              <tbody id="account-balance-table"></tbody>
            </table>
        </div>
    </div>
    <div class="col-3">
        <p>Balance</p>
        <div class="card" style="margin-bottom: 10px;">
            <div class="card-body">
                <div class="row">
                    <div class="col">
                        <p class="card-title" style="margin: 0px;">Income</p>
                    </div>
                    <div class="col">
                        <p class="a-table-income fw-bold fs-4" style="margin: 0px;"></p>
                    </div>
                </div>
            </div>
        </div>
        <div class="card" style="margin-bottom: 10px;">
            <div class="card-body">
                <div class="row">
                    <div class="col">
                        <p class="card-title" style="margin: 0px;">Outcome</p>
                    </div>
                    <div class="col">
                        <p class="a-table-outcome fw-bold fs-4" style="margin: 0px;"></p>
                    </div>
                </div>
            </div>
        </div>
        <div class="card" style="margin-bottom: 10px;">
            <div class="card-body">
                <div class="row">
                    <p>Categories</p>
                </div>
                <div>
                    <span class="badge rounded-pill text-bg-primary">Primary</span>
                    <span class="badge rounded-pill text-bg-secondary">Secondary</span>
                    <span class="badge rounded-pill text-bg-success">Success</span>
                    <span class="badge rounded-pill text-bg-danger">Danger</span>
                    <span class="badge rounded-pill text-bg-warning">Warning</span>
                </div>
            </div>
        </div>
    </div>
</div>