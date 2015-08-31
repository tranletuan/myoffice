 $(document).ready(function() {

 	var successLabel = "glyphicon-ok";
 	var errorLabel = "glyphicon-remove";

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

 	$('#oldPassword').focusin(function(){ $('.checkOldPassword').empty();});
	$('#newPassword').focusin(function(){ $('.checkNewPassword').empty();});
 	$('#confirmPassword').focusin(function(){ $('.checkConfirmPassword').empty();});


 	$('#oldPassword').focusout(function(){	
 		var URL = $(this).attr('data');
 		var oldPassword = $(this).val();
 		$('.checkOldPassword').empty();

 		$.ajax({
 			type : "GET",
 			url : URL,
 			data : {
 				"oldPassword" : oldPassword	
 			},
 			success: function(response){
 				if(response) {
 					$('.checkOldPassword').append("<span style='color:green;' class='glyphicon " + successLabel + "'></span>");
 				} else {
 					$('.checkOldPassword').append("<span style='color:red;' class='glyphicon " + errorLabel + "'></span>");
 				}

 				checkEnabledSubmit();
            }
        });
 	});

 	$('#newPassword').focusout(function(){	
 		var URL = $(this).attr('data');
 		var newPassword = $(this).val();
 		$('.checkNewPassword').empty();

 		$.ajax({
 			type : "GET",
 			url : URL,
 			data : {
 				"newPassword" : newPassword	
 			},
 			success: function(response){
 				if(response) {
 					$('.checkNewPassword').append("<span style='color:green;' class='glyphicon " + successLabel + "'></span>");
 				} else {
 					$('.checkNewPassword').append("<span style='color:red;' class='glyphicon " + errorLabel + "'></span>");
 				}

 				checkEnabledSubmit();
            }
        });
 	});

 	$('#confirmPassword').focusout(function(){	
 		var URL = $(this).attr('data');
 		var newPassword = $('#newPassword').val();
 		var confirmPassword = $(this).val();
 		$('.checkConfirmPassword').empty();

 		$.ajax({
 			type : "GET",
 			url : URL,
 			data : {
 				"newPassword" : newPassword,
 				"confirmPassword" : confirmPassword	
 			},
 			success: function(response){
 				if(response) {
 					$('.checkConfirmPassword').append("<span style='color:green;' class='glyphicon " + successLabel + "'></span>");
 				} else {
 					$('.checkConfirmPassword').append("<span style='color:red;' class='glyphicon " + errorLabel + "'></span>");
 				}

 				checkEnabledSubmit();
            }
        });
 	});

 	function checkEnabledSubmit(){
 		var successSpanClass = "." + successLabel;
 		var numSuccess = $(successSpanClass).length;
 		var numInput = $('#password-form input').length - 1;

 		if(numSuccess == numInput) {
 			$('#btnSubmit').removeAttr('disabled', false);
 			$('#btnSubmit').focus();
 		} else {
 			$('#btnSubmit').attr('disabled', true);
 		}	
 	};

});

