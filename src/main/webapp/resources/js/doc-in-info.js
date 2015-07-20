 $(document).ready(function() {

 	$('.modal').on('shown.bs.modal', function() {
	  $(this).find('[autofocus]').focus();
	});
	
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
 	var timeWidth = $('#numberRec').css('width');
 	$('#receiveTime').css('width', timeWidth);
 	
 	$(window).resize(function(){
 			timeWidth = $('#number').css('width');
 			$('#receiveTime').css('width', timeWidth);
 	});
 

 	function autoGeneratedNumber(){

 		var tenureId = $('.tenure').attr('data');
 		var organId = $('.rOrgan').attr('data');
 		var URL = $('#btnAutoGeneratedNumber').attr('data');

 		$('#btnAutoGeneratedNumber').button('loading');

 		$.ajax({
 			type : "GET",
 			url : URL,
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

 	$('#btnAssign').click(function(){
 		var strStart = $('#showTimeStart').html().split('-');
 		var strEnd = $('#showTimeEnd').html().split('-');
 		var content = $('#showContent').html();

 		$('.date-start').datetimepicker("setDate", new Date(strStart[2], strStart[1], strStart[0]));
 		$('.date-end').datetimepicker("setDate", new Date(strEnd[2], strEnd[1], strEnd[0]));
 		$('#content').html(content);
 	});

 	$('#btnTempReport').click(function(){
 		var report = $('#showReport').html();
 		var progressValue = $('.progressValue').html();

 		$("#v-slider").slider({
	 		orientation: "horizontal",
	 		range: "min",
	 		min: 0,
	 		max: 100,
	 		value: progressValue,
	 		slide: function (event, ui) {
	 			$("#amount").val(ui.value);
	 		}
	 	});

	 	$("#amount").val($("#v-slider").slider("value"));
 		$('#report').html(report);
 	});

 	$('#btn-change-doc-in').click(function() {

 		var title = $('.title').html();
 		var docName = $('.docName').html();
 		var epitome = $('.epitome').html();
 		var docType = $('.docType').attr('data');
 		var tenure = $('.tenure').attr('data');
 		var number = $('.number').html();
 		var numberSign = $('.numberSign').html();
 		var departments = $('.departments').html();
 		var privacyLevel = $('.privacyLevel').attr('data');
 		var emergencyLevel = $('.emergencyLevel').attr('data');
 		var releaseTime = $('.releaseTime').html();
 		var note = $('.note').html();

 		$('#title').val(title);
 		$('#docName').val(docName);
 		$('#epitome').val(epitome);
 		$('#docType').val(docType);
 		$('#tenure').val(tenure);
 		$('#number').val(number);
 		$('#numberSign').val(numberSign);
 		$('#departments').val(departments);
 		$('#privacyId').val(privacyLevel);
 		$('#emeId').val(emergencyLevel);
 		$('#releaseTime').val(releaseTime);
 		$('#note').val(note);

 	});

 	$("#amount").val($("#v-slider").slider("value"));
 	$("#amount").change(function(){
 		$("#v-slider").slider("value", $("#amount").val());
 	});
 });
