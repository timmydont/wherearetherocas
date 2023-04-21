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
        <canvas id="transactions-account-a"></canvas>
        <div class="transactions-account-a-table">
            <p>Income: <span class="a-table-income">123</span></p>
            <p>Outcome: <span class="a-table-outcome">123</span></p>

            <table id="account-a-table" class="table table-borderless table-striped table-earning">
              <thead>
                <tr>
                  <th>Item</th>
                  <th>Amount</th>
                </tr>
              </thead>
              <tbody></tbody>
            </table>
        </div>
    </div>
    <div class="row">
        <canvas id="transactions-account-b"></canvas>
            <div class="transactions-account-b-table">
            <p>Income: <span class="b-table-income">123</span></p>
            <p>Outcome: <span class="b-table-outcome">123</span></p>

            <table id="account-b-table" class="table table-borderless table-striped table-earning">
              <thead>
                <tr>
                  <th>Item</th>
                  <th>Amount</th>
                </tr>
              </thead>
              <tbody></tbody>
            </table>
        </div>
    </div>
</div>
