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
 		$('.btn-search').button('loading');

 		$.ajax({
 			type : $(this).attr('method'),
 			url : $(this).attr('action'),
 			data : $(this).serialize(),
 			success : function(response) {
 				console.log(response.jsTable)
 				$('#panel-staff-history').removeClass('hidden');
 				$('#panel-document-history').removeClass('hidden');
 				
 				$('#table-staff').bootstrapTable({data : response.jsTable});
 				showHighChart('#show-document-history', response.jsChart);
 				$('.btn-search').button('reset');
 			},
 			error : function(){
 				$('#panel-document-history').addClass('hidden');
 				$('#panel-staff-history').addClass('hidden');
 				$('.btn-search').button('reset');
 			}
 		});

 		e.preventDefault();
 	});

 	$('#basic-info-table').on('hidden.bs.collapse', function () {
  		$('#span-basic-info').attr('class', 'glyphicon glyphicon-chevron-up');
	});

	$('#basic-info-table').on('shown.bs.collapse', function () {
  		$('#span-basic-info').attr('class', 'glyphicon glyphicon-chevron-down');
	});

	function showHighChart(divName, jsData) {
	 	$(divName).highcharts({
	        chart: {
	            type: jsData.type
	        },
	        title: {
	            text: jsData.title
	        },
	        subtitle: {
	            text: jsData.subtitle
	        },
	        xAxis: {
	            categories: jsData.xAxisCategories,
	            title: {
	                text: jsData.xAxisTitle
	            }
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: jsData.yAxisTitle,
	                align: 'high'
	            },
	            labels: {
	                overflow: 'justify'
	            }
	        },
	        tooltip: {
	            valueSuffix: jsData.valueSuffix
	        },
	        plotOptions: {
	            bar: {
	                dataLabels: {
	                    enabled: true
	                }
	            }
	        },
	        legend: {
	            layout: jsData.legendLayout,
	            align: jsData.legendAlign,
	            verticalAlign: jsData.legendVerticalAlign,
	            x: jsData.legendX,
	            y: jsData.legendY,
	            floating: jsData.legendFloating,
	            borderWidth: jsData.legendBorderWidth,
	            backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
	            shadow: jsData.legendShadow
	        },
	        credits: {
	            enabled: false
	        },
	        series: jsData.series
	    });
	}

});