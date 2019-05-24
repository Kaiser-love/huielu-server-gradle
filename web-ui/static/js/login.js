$(document).ready(function() {
	var user = getStorage("user");
	if (user)
		removeStorage("user");
	
	function checkInfo(username, password) {
		if (isEmpty(username)) {
			showMessage("用户名不能为空");
			return false;
		}
		if (isEmpty(password)) {
			showMessage("密码不能为空");
			return false;
		}
		return true;
	}
	
	function autoRecognizeUsername(data) {
		//检查顺序手机号0、邮箱1、用户名2
        var phonePattern = /^\d{11}$/;
        if(phonePattern.test(data)){
            return 0;
        }
        //Email正则
        var emailPattern = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
		if(emailPattern.test(data)){
			return 1;
		}
		return 2;
	}
	
	$(".btnLogin").on('click', function() {
		var data = $("#username").val(),
			password = $("#password").val();

		if (!checkInfo(data, password))
			return false;
		var regResult = autoRecognizeUsername(data)
		var username = "";
		var phone = "";
		var email = "";
		if (regResult == 0)
			phone = data;
		else if (regResult == 1)
			email = data;
		else
			username = data;
		$.ajax({
			url: getFullUrl('/login'),
			type: 'post',
			data: {
				"username": username,
				"password": md5(password),
				"phone": phone,
				"email": email
			},
			dataType: 'json',
			success: function(json) {
				console.log(json);
				if (json.code == 0) {
					showMessage("登陆成功");
					setStorage("user", json.body);
					routerTo("/ui/userinfo.html");
				}
				else {
					showMessage("登陆失败：" + json.msg);
				}
			},
			error: function(json) {
				showMessage("登陆失败");
				console.log(json);
			}
		});
	});
});