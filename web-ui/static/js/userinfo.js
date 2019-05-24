$(document).ready(function () {
    var user = getStorage("user");

    if (user) {
        $("#navbarUsername").text(user.username);
        $("#navbarHead").attr("src", user.head);

        $("#head").attr("src", user.head);
        $("#nickname").val(user.nickname);
        if (user.sex == 0)
            $("#sex").val("男");
        else if (user.sex == 1)
            $("#sex").val("女");
        $("#birthday").val(user.birthday);
        $("#phone").val(user.phone);
        $("#email").val(user.email);
        $("#company").val(user.company);
        $("#title").val(user.title);
        $("#industry").val(user.industry);
    }
    else {
        logout();
    }

    $(".btnUserInfo").on('click', function () {
        routerTo("/ui/userinfo.html");

    });

    $(".btnPPT").on('click', function () {
        routerTo('/ui/directory.html');
    });

    $(".btnLogout").on('click', function () {
        logout();
    });

    $(".btnUpdate").on('click', function () {
        $(".form-control-plaintext").removeAttr("readonly")
        $(".form-control-plaintext").attr("class", "form-control");
        $(".btnUpdate").hide();
        $(".btnSave").show();
        // update sex radio
        $("#sex").hide();
        $(".sexRadio").show();
        if (user.sex == 0)
            $("#sexForMale").attr("checked", "checked");
        else if (user.sex == 1)
            $("#sexForFemale").attr("checked", "checked");
        // TODO: update datetimerpicker
        // add authcode input
        $(".form-group:eq(4)").show();
        $(".btnSendCode").on('click', function () {
            $(".btnSendCode").hide();
            $(".btnWait").show();
            // TODO: check phone
            var phone = $("#phone").val();
            var waittime = 60;
            $.ajax({
                url: getFullUrl('/requestauthcode'),
                type: 'post',
                data: {
                    "phone": phone
                },
                dataType: 'json',
                success: function (json) {
                    if (json.code == 0) {
                        showMessage('验证码发送成功');
                        waittime = json.body;
                        $("#lefttime").text(waittime);
                        var timer = null;
                        timer = setInterval(function () {
                            waittime -= 1;
                            if (waittime > 0) {
                                $('#lefttime').html(waittime);
                            }
                            else {
                                clearInterval(timer);
                                $(".btnSendCode").show();
                                $(".btnWait").hide();
                            }
                        }, 1000);
                    }
                    else {
                    		showMessage(json.msg);
                    }
                },
                error: function (json) {

                }
            });
        })
    })

    $(".btnSave").on('click', function () {
        var nickname = $("#nickname").val(),
            birthday = $("#birthday").val(),
            phone = $("#phone").val(),
            authcode = $("#authcode").val(),
            email = $("#email").val(),
            company = $("#company").val(),
            title = $("#title").val(),
            industry = $("#industry").val();
        var sex = 0;
        // BUG: 获取性别选项
        if ($("#sexForMale"))
            sex = 0;
        else
            sex = 1;
        // BUG: 检查手机号变化后，验证码是否输入
        var uid = user.uid;
        $.ajax({
            url: getFullUrl('/updateuserinfo'),
            type: 'post',
            data: {
                "uid": uid,
                "nickname": nickname,
                "sex": sex,
                "birthday": birthday,
                "phone": phone,
                "authcode": authcode,
                "email": email,
                "company": company,
                "title": title,
                "industry": industry
            },
            dataType: 'json',
            success: function (json) {
                if (json.code == 0) {
                    showMessage('更新成功');
                    console.log(json);
                    setStorage('user', json.body);
                    routerTo("/ui/userinfo.html");
                }
                else {
                		showMessage("更新失败：" + json.msg);
	                	if (json.code == 6)
	                		routerTo("/ui/login.html");
                }
            },
            error: function (json) {

            }
        });

    })
});

