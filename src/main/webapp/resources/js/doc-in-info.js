 $(document).ready(function() {

 	$('#btnAutoGeneratedNumber').click(autoGeneratedNumber);
 	
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

 	function autoGeneratedNumber(){

 		var docType = $('#docType option:selected').attr('class');
 		var organ = $('#organ').attr('class');

 		var tenureId = $('#tenure option:selected').val();
 		var docTypeId = $('#docType').val();
 		var organId = $('#organId').html();

 		$('#btnAutoGeneratedNumber').button('loading');

 		$.ajax({
 			type : "GET",
 			url : "number_in",
 			data : {
 				"tenureId" : tenureId,
 				"organId" : organId
 			},
 			success: function(response){
 				$('#number').val(response);
  				$('#btnAutoGeneratedNumber').button('reset');
            }
        });
 	}

 });
