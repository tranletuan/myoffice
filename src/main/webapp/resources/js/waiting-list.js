$(document).ready(function() {

	$('.wait-link').click(function(ev){

 		$.ajax({
 			type : 'GET',
 			url : $(this).attr('href'),
 			data : $(this).serialize(),
 			success : function(response) {
 				$('#show-content').html(response);
 			}
 		});

 		ev.preventDefault();
 	}); 


});