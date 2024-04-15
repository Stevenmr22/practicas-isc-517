function getRandomColor() {
  var r = Math.floor(Math.random() * 256);
  var g = Math.floor(Math.random() * 256);
  var b = Math.floor(Math.random() * 256);
  return 'rgba(' + r + ',' + g + ',' + b + ', 0.2)';
}

function getRandomColorBorder() {
  var r = Math.floor(Math.random() * 256);
  var g = Math.floor(Math.random() * 256);
  var b = Math.floor(Math.random() * 256);
  return 'rgba(' + r + ',' + g + ',' + b + ', 1)';
}

$(function() {
  'use strict';
  var doughnutPieOptions = {
    responsive: true,
    animation: {
      animateScale: true,
      animateRotate: true
    }
  };

  var data = {
    labels: accessHours,  // Use the new list of LocalDateTime strings (truncated to hours)
    datasets: [{
      label: '# de Clicks',
      data: accessCounts,  // Use the new list of counts
      backgroundColor: accessHours.map(() => getRandomColor()),
      borderColor: accessHours.map(() => getRandomColorBorder()),
      borderWidth: 1,
      fill: false
    }]
  };

  var options = {
    scales: {
      yAxes: [{
        ticks: {
          beginAtZero: true
        }
      }]
    },
    legend: {
      display: false
    },
    elements: {
      point: {
        radius: 0
      }
    }
  };

  if ($("#barChart").length) {
    var barChartCanvas = $("#barChart").get(0).getContext("2d");
    var barChart = new Chart(barChartCanvas, {
      type: 'bar',
      data: data,
      options: options
    });
  }

  if ($("#doughnutChart").length) {
    var osDoughnutChartCanvas = $("#doughnutChart").get(0).getContext("2d");
    var osData = {
      labels: Object.keys(accessOS),
      datasets: [{
        data: Object.values(accessOS),
        backgroundColor: Object.keys(accessOS).map(() => getRandomColor()),
        borderColor: Object.keys(accessOS).map(() => getRandomColorBorder()),
        borderWidth: 1
      }]
    };
    var osDoughnutChart = new Chart(osDoughnutChartCanvas, {
      type: 'pie',
      data: osData,
      options: doughnutPieOptions
    });
  }
  if ($("#browserDoughnutChart").length) {
    var browserDoughnutChartCanvas = $("#browserDoughnutChart").get(0).getContext("2d");
    var browserData = {
      labels: Object.keys(browser),
      datasets: [{
        data: Object.values(browser),
        backgroundColor: Object.keys(browser).map(() => getRandomColor()),
        borderColor: Object.keys(browser).map(() => getRandomColorBorder()),
        borderWidth: 1
      }]
    };
    var browserDoughnutChart = new Chart(browserDoughnutChartCanvas, {
      type: 'doughnut',
      data: browserData,
      options: doughnutPieOptions
    });
  }
});