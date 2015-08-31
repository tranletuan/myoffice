 $(document).ready(function() {
 	var unitId = -1;
 	var unitName;
 	var address;
 	var shortName;
 	
 	$('.modal').on('shown.bs.modal', function() {
	  $(this).find('[autofocus]').focus();
	});

 	$("#table-body").remove();

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		unitId = $(this).find('.unitId').html();
 		unitName = $(this).find('.unitName').html();
 		address = $(this).find('.address').html();
 		shortName = $(this).find('.shortName').html();
 		
 		if(unitId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});


 	$('#btn-change').click(function(){
 		$('#unitId').val(unitId);
 		$('#unitName').val(unitName);
 		$('#address').val(address);
 		$('#shortName').val(shortName);
 		
 	});

 	$('#btn-add').click(function(){
 		$('#unitId').val("-1");
 		$('#unitName').val("");
 		$('#address').val("");
 		$('#shortName').val("");
 	});

 });