package com.ronghui.server.network;

import java.io.Serializable;

import com.ronghui.server.files.network.ResCode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Data
public class Message<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T body;
    // -1			internal error
    // 0				success
    // 1				user doesn't exist
    // 2				request for auth code is too frequent
    // 3				phone number to login is wrong
    // 4				authcode is invalid
    // 5				authcode is wrong
    // 6				username or password is wrong
    // 7				email exist
    // 8				not login
//     9                PPT NAME is empty
//     10               dir name is empty
    // 11               wechat register has phone

    private int code;
    private String msg;

    public void setResCode(ResCode res) {
        code = res.getCode();
        msg = res.getMsg();
    }
}
