# 日程表

## 一期

总截止日期：2018.09.10——2018.10.20

| 界面/功能点                                     | 涉及端   |
| ----------------------------------------------- | -------- |
| 界面：首页                                      | WEB      |
| 界面：用户注册                                  | WEB      |
| 界面：用户登陆                                  | WEB      |
| 界面：用户忘记密码                              | WEB      |
| 界面：第三方QQ                                  | WEB      |
| 界面：第三方微信                                | WEB      |
| 界面：个人资料首页（包括修改信息的modal）       | WEB      |
| 界面：相册目录首页（包括添加、删除目录的modal） | WEB      |
| 界面：图片列表页（包括图片查看的modal）         | WEB      |
| 界面：图片调整modal                             | WEB      |
| 界面：生成PPT首页                               | WEB      |
| 界面：PPT查看modal                              | WEB      |
| 界面：生成PPT界面                               | WEB      |
| 功能：进入首页                                  | WEB      |
| 功能：进入注册页                                | WEB      |
| 功能：进入登陆页                                | WEB      |
| 功能：检查用户名重复                            | WEB      |
| 功能：发送手机验证码                            | WEB      |
| 功能：注册                                      | WEB      |
| 功能：用户名/邮箱登陆                           | WEB      |
| 功能：忘记密码                                  | WEB      |
| 功能：进入个人资料页                            | WEB      |
| 功能：上传头像                                  | WEB      |
| 功能：更新个人资料                              | WEB      |
| 功能：更新密码                                  | WEB      |
| 功能：获取用户所有目录                          | WEB, APP |
| 功能：新建目录                                  | WEB, APP |
| 功能：更新目录                                  | WEB, APP |
| 功能：删除目录                                  | WEB, APP |
| 功能：查找目录                                  | WEB      |
| 功能：获取目录所有图片                          | WEB, APP |
| 功能：保存图片                                  | WEB, APP |
| 功能：删除图片                                  | APP      |
| 功能：移动图片                                  | APP      |
| 功能：获取用户所有PPT                           | WEB      |
| 功能：保存PPT                                   | WEB, APP |
| 功能：删除PPT                                   | WEB, APP |
| 功能：检查openid                                | WECHAT   |

# 前后端传输协议

类型：JSON

格式：

```json
{
    body: map<string, object>,
    code: int,
    message: string
}
```

| code | 结果 |
| ---- | ---- |
| 0    | 成功 |

# 交互接口

* 功能：进入首页；SUCCESS

  url: index

  request: null

  response: null

* 功能：进入注册页；SUCCESS

  url: getregister

  request: null

  response: null

* 功能：进入登陆页；SUCCESS

  url: getlogin

  request: null

  response: null

* 功能：检查用户名重复；SUCCESS

  url: checkusername

  request: {

  ​	username

  }

  reponse: {

  ​	exist

  }

* 功能：发送手机验证码

  url: requestauthcode

  request: {

  ​	phone

  }

  response: {

  ​	waittime

  }

* 功能：注册；SUCCESS

  url: register

  request: {

  ​	username, 

  ​	password,

  ​	phone,

  ​	email,

  ​	authcode

  }

  respone: null

* 功能：获取用户头像

  url:requesthead

  request: {

  ​	username,

  ​	phone,

  ​	email

  }

  response: {

  ​	head

  }

* 功能：登陆

  * WITH PASSWORD

  url: login

  request: {

  ​	username,

  ​	password,

  ​	phone,

  ​	email

  }

  response: {

  ​	user

  }

  * WITH AUTHCODE

  url: loginwithauthcode

  request: {

  ​	phone,

  ​	authcode

  }

  response: {

  ​	user,

  ​	new

  }

* 功能：忘记密码

* 功能：进入个人资料页；SUCCESS

  url: getuserinfo

  request: null

  response: null

* 功能：上传头像

* 功能：更新个人资料；SUCCESS

  url: updateuserinfo

  request: {

  ​	uid,

  ​	nickname,

  ​	phone,

  ​	email,

  ​	birthday,

  ​	sex,

  ​	nickname,

  ​	authcode,

  ​	company,

  ​	title,

  ​	industry

  }

  reponse: null

* 功能：更新密码

  url: updatepassword

  request: {

  ​	oldPassword

  ​	newPassword

  }

  reponse: null

* 功能：获取用户所有目录

  url:getdirectorylist

  request: null

  response: {

  ​	dirList: [{

  ​		dirid,

  ​		name,

  ​		picPath,

  ​		picCount		

  ​	}]

  }

* 功能：新建目录

  url:createdirectory

  request: {

  ​	name

  }

  response: null

* 功能：更新目录

  url: updatedirectory

  request: {

  ​	dirid,

  ​	name,

  }

  response: null

* 功能：删除目录

  url: deletediretory

  request: {

  ​	dirid

  }

  response: null

* 功能：获取目录所有图片

  url: getpicturelist

  request: {

  ​	dirid

  }

  response: {

  ​	picList: [{

  ​		picid,

  ​		path

  ​	}]

  }

* 功能：上传图片

  url: uploadpicture

  request: {

  ​	dirid,

  ​	path

  }

  response: null

* 功能：移动图片

  url: updatepicture

  request: {

  ​	dirid,

  ​	picid

  }

  respone: null

* 功能：删除图片

  url: deletepicture

  request: {

  ​	picid

  }

  respone: null

* 功能：检查openid

  url: checkuser

  request: {

  ​	openid,

  }

  respone: {

  ​	user

  }

* 功能：保存PPT

  url:saveppt

  request: {

  ​	urlList: [],

  }

* 获取用户所有PPT

  url: getpptlist

  request: null

  response: {

  ​	pptList: {[

  ​		pptid,

  ​		

  ​	]}

  }

* 