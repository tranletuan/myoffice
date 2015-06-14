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

 	function autoGeneratedNumber(){

 		var docType = $('#docType option:selected').attr('class');
 		var organ = $('#organ').attr('class');

 		var tenureId = $('#tenure option:selected').val();
 		var docTypeId = $('#docType').val();

 		$(this).button('loading');

 		$.ajax({
 			type : "GET",
 			url : "number",
 			data : {
 				"tenureId" : tenureId,
 				"docTypeId" : docTypeId
 			},
 			success: function(response){
 				$('#number').val(response);
               	var numberSign =  '-' + docType + '/' + organ + '-';
  				$('#numberSign').val(numberSign);
  				$(this).button('reset');
            }
        });

 		

 	}

 });