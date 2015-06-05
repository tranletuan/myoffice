/**
 * 
 */

 $(document).ready(function() {
 	var roleId = -1;
 	var roleName;
 	var fullName;
 	var shortName;

 	/*$("table.short_table").short_table({
        "action" : "init"
    });*/

    $("#table-body").remove();

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		roleId = $(this).find('.roleId').html();
 		roleName = $(this).find('.roleName').html();
 		fullName = $(this).find('.fullName').html();
 		shortName = $(this).find('.shortName').html();

 		if(roleId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});


 	$('#btn-change').click(function(){
 		$('#roleId').val(roleId);
 		$('#roleName').val(roleName);
 		$('#fullName').val(fullName);
 		$('#shortName').val(shortName);
 	});

 	$('#btn-add').click(function(){
 		$('#roleId').val("-1");
 		$('#roleName').val("");
 		$('#fullName').val("");
 		$('#shortName').val("");
 	});

 	
 });