$(document).ready(function() {

    var URL = $('#calendar').attr('data');
    $('#calendar').fullCalendar({
        header : {
        	left : 'prev,next today',
        	center : 'title',
        	right : 'month,basicWeek'
        },
        eventLimit: true,
        eventSources: [
        {
        	url: URL,
        	type: 'GET',
        	data: {
        		custom_param1: 'something',
        		custom_param2: 'somethingelse'
        	},
        	error: function() {
        		alert('there was an error while fetching events!');
        	},
            color: 'yellow',   // a non-ajax option
            textColor: 'black' // a non-ajax option
        }

        // any other sources...

        ]
    });
    
});