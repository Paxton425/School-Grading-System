//Charts
// Add a global font color for light mode
Chart.defaults.color = '#64748b';
Chart.defaults.font.family = "'Segoe UI', sans-serif";

function buildTrendChart(data){
    new Chart(document.getElementById('trendChart'), {
        type: 'line',
        data: {
            labels: ['Jan','Feb','Mar','Apr','May'],
            datasets: [{
                label: 'Avg Score',
                data: [65,70,68,75,80],
                borderColor: '#0a80c6',
                backgroundColor: 'rgba(19,143,214,0.2)', // Light blue fill
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            layout: {
                padding: {
                    left: 5,
                    right: 5,
                    top: 10,
                    bottom: 0
                }
            },
            plugins: { legend: { display: false } },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: { color: '#f8fafc' }, // Subtle grid lines
                    ticks: { padding: 10 }      // Space between numbers and axis
                },
                x: { grid: { display: false } }
            }
        }
    });
}

function buildGradesDistChart(data){
    const gradeDistribution = data.gradeDistribution;
    new Chart(document.getElementById('gradeChart'), {
        type: 'doughnut',
        data: {
            labels: ['Excellent','Good','Average','Bad','poor'],
            datasets: [{
                data: [
                    gradeDistribution['excellent'],
                    gradeDistribution['good'],
                    gradeDistribution['average'],
                    gradeDistribution['bad'],
                    gradeDistribution['poor']
                ],
                backgroundColor: ['#0e750e','#1aa51a','#117ec1','#ffae00','#980900FF'],
                borderWidth: 0
            }]
        },
        options: {
            plugins: { legend: { position: 'bottom' } }
        }
    });
}

function buildPerfomanceChart(data){
    const labels = Object.keys(data['subjectPerformance']);
    const values = Object.values(data['subjectPerformance']);

    new Chart(document.getElementById('barChart'), {
        type: 'bar',
        data: {
            labels: ['Maths','Physics', 'Chemistry','History','English'],
            datasets: [{
                data: values,
                backgroundColor: '#3b82f6',
                borderRadius: 2,
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false, // Allows it to fill the height you give it
            plugins: {
                legend: { display: false }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    min: 0,
                    max: 100,
                    ticks: {
                        stepSize: 20,
                        maxTicksLimit: 6, // Forces 0, 20, 40, 60, 80, 100
                        autoSkip: false   // Prevents Chart.js from "being smart" and hiding labels
                    }
                },
                x: {
                    grid: { display: false }
                }
            }
        }
    });
}

//Get Dashboard data
fetch("http://localhost:8080/dashboard/data")
    .then(response => response.json())
    .then(dashboardData => {
        if(dashboardData != null && dashboardData != ""){
            console.log('dashboard data: ', dashboardData);
            buildTrendChart(dashboardData);
            buildGradesDistChart(dashboardData);
            buildPerfomanceChart(dashboardData);
            animateKPIs(dashboardData.stats);
        }
    })
    .catch(error => {
        console.error("Dashboard fetch failed:", error);
        alert('Something went wrong: ' + error.message);
    });


function animateKPIs(stats) {
    animateValue(
        document.getElementById('studentsCount'),
        0,
        stats.studentsCount,
        1000
    );

    animateValue(
        document.getElementById('averageGrade'),
        0,
        stats.averageGrade,
        1200,
        '%'
    );

    animateValue(
        document.getElementById('passRate'),
        0,
        stats.passRate,
        1200,
        '%'
    );
}

function animateValue(el, start, end, duration, suffix = '') {
    let startTimestamp = null;

    function step(timestamp) {
        if (!startTimestamp) startTimestamp = timestamp;
        const progress = Math.min((timestamp - startTimestamp) / duration, 1);

        const value = Math.floor(progress * (end - start) + start);
        el.textContent = value + suffix;

        if (progress < 1) {
            window.requestAnimationFrame(step);
        }
    }

    window.requestAnimationFrame(step);
}

