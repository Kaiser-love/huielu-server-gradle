package com.ronghui.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class Util {
    private static final Logger log = LoggerFactory.getLogger(Util.class);

    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static final String product = "Dysmsapi";
    private static final String domain = "dysmsapi.aliyuncs.com";
    private static final String accessKeyId = "LTAIFPXGzVz1u5HT";
    private static final String accessKeySecret = "H2UinkHcpw8ZC1R8QVcpcID7OS4ei6";

    public static final String WECHAT_APPID = "wx261ebcde2b79f442";
    public static final String WECHAT_APPSECRET = "f6baa46efc9e508e861ccf48eaeee67e";


    public static String serialize(Object obj) {
        return "";
    }

    public static String generateRandomCode(int dight) {
        String code = "";
        Random random = new Random();
        for (int i = 0; i < dight; ++i)
            code = code + Integer.toString(random.nextInt(10));
        code = "394187";
        return code;
    }

    public static String stringToMD5(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md = md5.digest(str.getBytes());
            char res[] = new char[md.length * 2];
            for (int i = 0, k = 0; i < md.length; i++) {
                byte byte0 = md[i];
                res[k++] = hexDigits[byte0 >>> 4 & 0xf];
                res[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(res);
        } catch (NoSuchAlgorithmException e) {
            log.error("NO MD5 ALGORITHM! MSG:" + e.getMessage());
            return null;
        }
    }

    public static boolean sendAuthCode(String phone, String authcode) {
        if (phone != null)
            return true;
        try {
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            // 组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            // 使用post提交
            request.setMethod(MethodType.POST);
            // 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
            request.setPhoneNumbers(phone);
            // 必填:短信签名-可在短信控制台中找到
            request.setSignName("starfire星火");
            // 必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
            request.setTemplateCode("SMS_149100481");
            // 可选:模板中的变量替换JSON串,如模板内容为"您的验证码为${code}"时,此处的值为
            // 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            request.setTemplateParam("{\"code\":\"" + authcode + "\"}");
            // 可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
            // request.setSmsUpExtendCode("90997");
            // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            // request.setOutId("");
            // 请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK"))
                return true;
        } catch (ClientException e) {
            log.error("CLIENT EXCEPTION: CODE:" + e.getErrCode() + "\tMSG:" + e.getErrMsg());
        }
        return false;
    }
    
    public static String getOpenId(String appid, String secret, String code) {
    		String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + WECHAT_APPID + "&secret=" + WECHAT_APPSECRET + "&grant_type=authorization_code&js_code=" + code;
    		String openid = "";
    		try {
	    		URL realUrl = new URL(url);
	    		URLConnection connection = realUrl.openConnection();
	    		connection.connect();
	    		InputStream inputStream = connection.getInputStream();
	    		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	    		BufferedReader reader = new BufferedReader(inputStreamReader);
	    		String tempLine = reader.readLine();
	    		if (tempLine == null)
	    			return null;
	    		JSONObject jo = JSONObject.parseObject(tempLine);
	    		log.error(tempLine);
	    		openid = jo.getString("openid");
    		} catch (MalformedURLException e) {
    			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    		return openid;
    }

    public static String createToken(String username, String password) {
        return stringToMD5(username +
                password +
                LocalDateTime.now().toString());
    }

    public static String base64Encode(String base64Value) {
        try {
            return Base64.getEncoder().encodeToString(base64Value.getBytes(StandardCharsets.UTF_8));        }
        catch (Exception e) {
            return null;
        }
    }

}
