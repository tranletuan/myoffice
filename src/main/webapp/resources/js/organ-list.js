 $(document).ready(function() {
 	var organId = -1;
 	var organName;
 	var email;
 	var phoneNumber;
 	var shortName;
 	var unitId;
 	var organTypeId;

 	/*$("table.sort_table").sort_table({
 		"action" : "init"
 	});*/

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		organId = $(this).find('.organId').html();
 		organName = $(this).find('.organName').html();
 		email = $(this).find('.email').html();
 		phoneNumber = $(this).find('.phoneNumber').html();
 		shortName = $(this).find('.shortName').html();
 		unitId = $(this).find('.unit').attr('data');
 		organTypeId = $(this).find('.organType').attr('data');

 		if(organId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});


 	$('#btn-change').click(function(){
 		$('#organId').val(organId);
 		$('#organName').val(organName);
 		$('#phoneNumber').val(phoneNumber);
 		$('#email').val(email);
 		$('#shortName').val(shortName);

 		if(unitId > 0){
 			$('#unit').val(unitId);
 		}

 		if(organTypeId > 0){
 			$('#organType').val(organTypeId);
 		}
 	});

 	$('#btn-add').click(function(){
 		$('#organId').val("-1");
 		$('#organName').val("");
 		$('#phoneNumber').val("");
 		$('#email').val("");
 		$('#shortName').val("");
 		$('#unit').val(1);
 		$('#organType').val(1);
 	});

 	$("#table-body").remove();


 });