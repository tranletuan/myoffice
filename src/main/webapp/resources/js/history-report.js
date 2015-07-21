$(document).ready(function(){

	$('.form_date').datetimepicker({
 		format : 'dd-mm-yyyy',
 		weekStart: 1,
 		todayBtn:  1,
 		autoclose: 1,
 		todayHighlight: 1,
 		startView: 2,
 		minView: 2,
 		forceParse: 0,
 		initialDate : new Date()
 	});
 	
 	$('.form_date').datetimepicker("setDate", new Date());

 	$('#history-form').submit(function(e){

 		$.ajax({
 			type : $(this).attr('method'),
 			url : $(this).attr('action'),
 			data : $(this).serialize(),
 			success : function(response) {
 				$('#show-history').html(response);
 				$('.btn-search').button('reset');
 			},
 			error : function(){
 				$('.btn-search').button('reset');
 			}
 		});

 		e.preventDefault();
 	});

 	$('#show-history').highcharts({
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Historic World Population by Region'
        },
        subtitle: {
            text: 'Source: <a href="https://en.wikipedia.org/wiki/World_population">Wikipedia.org</a>'
        },
        xAxis: {
            categories: ['Africa', 'America', 'Asia', 'Europe', 'Oceania'],
            title: {
                text: null
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Population (millions)',
                align: 'high'
            },
            labels: {
                overflow: 'justify'
            }
        },
        /*tooltip: {
            valueSuffix: ' millions'
        },*/
        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -40,
            y: 80,
            floating: true,
            borderWidth: 1,
            backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
            shadow: true
        },
        credits: {
            enabled: false
        },
        series: [{
            name: 'Year 1800',
            data: [107, 31, 635, 203, 2]
        }, {
            name: 'Year 1900',
            data: [133, 156, 947, 408, 6]
        }, {
            name: 'Year 2012',
            data: [1052, 954, 4250, 740, 38]
        }]
    });

});