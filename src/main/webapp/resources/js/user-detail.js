 $(document).ready(function() {

	$('.modal').on('shown.bs.modal', function() {
	  $(this).find('[autofocus]').focus();
	});
	
 	$('#btnChange').click(function(){
 		var fullName = $('.fullName').html();
 		var address = $('.address').html();
 		var phoneNumber = $('.phoneNumber').html();
 		var email = $('.email').html();
 		
 		$('#fullName').val(fullName);
 		$('#address').val(address);
 		$('#phoneNumber').val(phoneNumber);
 		$('#email').val(email);
 	});
});

