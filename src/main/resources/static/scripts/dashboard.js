// Add a global font color for light mode
Chart.defaults.color = '#64748b';
Chart.defaults.font.family = "'Segoe UI', sans-serif";

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

new Chart(document.getElementById('gradeChart'), {
    type: 'doughnut',
    data: {
        labels: ['Excellent','Good','Average','Bad','poor'],
        datasets: [{
            data: [10,20,15,8,3],
            backgroundColor: ['#0e750e','#1aa51a','#117ec1','#fb923c','#980900FF'],
            borderWidth: 0
        }]
    },
    options: {
        cutout: '75%',
        plugins: { legend: { position: 'bottom' } }
    }
});

new Chart(document.getElementById('barChart'), {
    type: 'bar',
    data: {
        labels: ['Maths','Science','History','English'],
        datasets: [{
            data: [78,85,72,80],
            backgroundColor: '#3b82f6',
            borderRadius: 6``
        }]
    },
    options: {
        plugins: { legend: { display: false } },
        scales: {
            y: { beginAtZero: true, grid: { color: '#f1f5f9' } },
            x: { grid: { display: false } }
        }
    }
});