 $(document).ready(function() {


 	$('.modal').on('shown.bs.modal', function() {
 		$(this).find('[autofocus]').focus();
 	});

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

 	

 });