$(document).ready(function() {
	
	var user = getStorage("user");
	
	if (user) {
		$("#navbarUsername").text(user.username);
		$("#navbarHead").attr("src", user.head);
	}
	else {
		logout();
	}
	
	$(".btnUserInfo").on('click', function() {
        routerTo("/ui/userinfo.html");
    });
	
	$(".btnPPT").on('click', function() {
        routerTo('/ui/directory.html');
	});
	
	$(".btnLogout").on('click', function() {
		logout();
	});
	
	$("#btnAddDirectory").on('click', function() {
		$('#add_directory').modal();
	});

});

//删除相册
function delDirectory() {  
    $('#del_directory').modal();  
}  


//跳转
function linkPicture() {  
     window.location.href="javascript:;";  
}  
