/**
 * 
 */

 $(document).ready(function() {
 	var roleId = -1;
 	var roleName;
 	var fullName;
 	var sortName;

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		roleId = $(this).find('.roleId').html();
 		roleName = $(this).find('.roleName').html();
 		fullName = $(this).find('.fullName').html();
 		sortName = $(this).find('.sortName').html();

 		if(roleId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});


 	$('#btn-change').click(function(){
 		$('#roleId').val(roleId);
 		$('#roleName').val(roleName);
 		$('#fullName').val(fullName);
 		$('#sortName').val(sortName);

 		console.log(roleId);
 		console.log(roleName);
 		console.log(fullName);
 		console.log(sortName);
 	});

 	$('#btn-add').click(function(){
 		$('#roleId').attr('value', '-1');
 		$('#roleName').removeAttr('value');
 		$('#fullName').removeAttr('value');
 		$('#sortName').removeAttr('value');
 	});

 	$("table.sort_table").sort_table({
        "action" : "init"
    });

    $("#table-body").remove();
 });