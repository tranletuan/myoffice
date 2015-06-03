/**
 * 
 */

$(document).ready(function() {

	var userId;
	var userName;
	var unit;
	var enabled;

	$('.table tr').click(function() {
		$('.table tr').attr('class', ' ');
		$(this).attr('class', 'active');

		userId = $(this).attr('id');
		userName = $(this).find('.userName').html();
		unit = $(this).find('.unit').html();
		enabled = $(this).find('.enabled').html();
	});
	
	$('#btn-change').click(function(){
		$('#userId').attr('value', userId);
		$('#userName').val(userName);
		$('#enabled').attr('checked', enabled);

		var unitList = $('#unit').find('option');
		for(var i = 0; i < unitList.length; i++){

			if(unitList[i].innerText == unit){
				unitList[i].setAttribute('selected', 'true');
				break;
			}
		}
	});
});