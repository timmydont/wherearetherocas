<html>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.9.1/chart.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3" crossorigin="anonymous"></script>
<body>
<h2>Hello World!</h2>
<div class="container">
<canvas id="myChart"></canvas>
</div>
</body>
<script>
const ctx = document.getElementById('myChart');
const myChart = new Chart(ctx, {
    type: 'pie',
    data: {
        labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple'],
        datasets: [{
            label: '# of Votes',
            data: [12, 19, 3, 5, 2, 3],
            backgroundColor: [
                'rgba(255, 99, 132, 0.9)',
                'rgba(54, 162, 235, 0.9)',
                'rgba(255, 206, 86, 0.9)',
                'rgba(75, 192, 192, 0.9)',
                'rgba(153, 102, 255, 0.9)',
                'rgba(255, 159, 64, 0.9)'
            ],
        },
        {
           label: '# of Votes2',
           data: [5, 9, 13, 9, 12, 3],
           backgroundColor: [
               'rgba(255, 99, 132, 0.9)',
               'rgba(54, 162, 235, 0.9)',
               'rgba(255, 206, 86, 0.9)',
               'rgba(75, 192, 192, 0.9)',
               'rgba(153, 102, 255, 0.9)',
               'rgba(255, 159, 64, 0.9)'
           ],
       },
        {
           label: '# of Votes2',
           data: [5, 9, 13, 9, 12, 3],
           backgroundColor: [
               'rgba(255, 99, 132, 0.9)',
               'rgba(54, 162, 235, 0.9)',
               'rgba(255, 206, 86, 0.9)',
               'rgba(75, 192, 192, 0.9)',
               'rgba(153, 102, 255, 0.9)',
               'rgba(255, 159, 64, 0.9)'
           ],
       }]
    },
    options: {
        responsive: true,
            plugins: {
                legend: {
                position: 'top',
            },
            title: {
              display: true,
              text: 'Chart.js Pie Chart'
            }
        }
    },
});
</script>
</html>
