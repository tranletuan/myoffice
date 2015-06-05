 $(document).ready(function() {
 	var unitId = -1;
 	var unitName;
 	var phoneNumber;
 	var email;
 	var organId;
 	var shortName;

 	/*$("table.sort_table").sort_table({
 		"action" : "init"
 	});*/

 	$("#table-body").remove();

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		unitId = $(this).find('.unitId').html();
 		unitName = $(this).find('.unitName').html();
 		phoneNumber = $(this).find('.phoneNumber').html();
 		email = $(this).find('.email').html();
 		organId = $(this).find('.organId').attr('id');
 		shortName = $(this).find('.shortName').html();

 		if(unitId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});


 	$('#btn-change').click(function(){
 		$('#unitId').val(unitId);
 		$('#unitName').val(unitName);
 		$('#phoneNumber').val(phoneNumber);
 		$('#email').val(email);
 		$('#organ').val(organId);
 		$('#shortName').val(shortName);
 	});

 	$('#btn-add').click(function(){
 		$('#unitId').val("-1");
 		$('#unitName').val("");
 		$('#phoneNumber').val("");
 		$('#email').val("");
 		$('#shortName').val("");
 	});

 });