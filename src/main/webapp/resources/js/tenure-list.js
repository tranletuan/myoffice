 $(document).ready(function() {
 	var tenureId = -1;
 	var tenureName;
 	var timeStart;
 	var timeEnd;

 	$('.modal').on('shown.bs.modal', function() {
 		$(this).find('[autofocus]').focus();
 	});

 	
 	$("#table-body").remove();

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		tenureId = $(this).find('.tenureId').html();
 		tenureName = $(this).find('.tenureName').html();
 		timeStart = $(this).find(".timeStart").html();
 		timeEnd = $(this).find(".timeEnd").html();

 		if(tenureId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});


 	$('#btn-change').click(function(){
 		$('#tenureId').val(tenureId);
 		$('#tenureName').val(tenureName);
 		
 		var strStart = timeStart.split('-');
 		var strEnd = timeEnd.split('-');

 		/*$('#timeStart').val($('#showTimeStart').html());
 		$('#timeEnd').val($('#showTimeEnd').html());*/

 		$('.date-start').datetimepicker('setDate', new Date(strStart[2], strStart[1] - 1, strStart[0]));
 		$('.date-end').datetimepicker('setDate', new Date(strEnd[2], strEnd[1] - 1, strEnd[0]));


 	});

 	$('#btn-add').click(function(){
 		$('#tenureId').val("-1");
 		$('#tenureName').val("");
 		$('#timeStart').val("");
 		$('#timeEnd').val("");
 		$('.date').datetimepicker('setDate', new Date());

 	});

 	$('.form_date').datetimepicker({
 		format : 'dd-mm-yyyy',
 		weekStart: 1,
 		todayBtn:  1,
 		autoclose: 1,
 		todayHighlight: 1,
 		startView: 2,
 		minView: 2,
 		forceParse: 0
 	});
 });