 $(document).ready(function() {
 	var emeId = -1;
 	var emeName;
 	var description;

 	$('.modal').on('shown.bs.modal', function() {
	  $(this).find('[autofocus]').focus();
	});
	
 	$("#table-body").remove();

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		emeId = $(this).find('.emeId').html();
 		emeName = $(this).find('.emeName').html();
 		description = $(this).find(".description").html();

 		if(emeId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});


 	$('#btn-change').click(function(){
 		$('#emeId').val(emeId);
 		$('#emeName').val(emeName);
 		$('#description').val(description);

 	});

 	$('#btn-add').click(function(){
 		$('#emeId').val("-1");
 		$('#emeName').val("");
 		$('#description').val(" ");

 	});

 });