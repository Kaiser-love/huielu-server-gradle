$(document).ready(function() {
	$('.loginPage').on('click', function(event) {
		routerTo('/ui/login.html')
	});
	
	$('.registerPage').on('click', function(event) {
		routerTo('/ui/loginphone.html')
	});
});