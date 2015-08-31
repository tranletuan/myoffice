 $(document).ready(function() {
 	var organTypeId = -1;
 	var organTypeName;
 	var shortName;

 	$('.modal').on('shown.bs.modal', function() {
	  $(this).find('[autofocus]').focus();
	});

 	$("#table-body").remove();

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		organTypeId = $(this).find('.organTypeId').html();
 		organTypeName = $(this).find('.organTypeName').html();
 		shortName = $(this).find('.shortName').html();

 		if(organTypeId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});


 	$('#btn-change').click(function(){
 		$('#organTypeId').val(organTypeId);
 		$('#organTypeName').val(organTypeName);
 		$('#shortName').val(shortName);
 	});

 	$('#btn-add').click(function(){
 		$('#organTypeId').val("-1");
 		$('#organTypeName').val("");
 		$('#shortName').val("");
 	});

 });