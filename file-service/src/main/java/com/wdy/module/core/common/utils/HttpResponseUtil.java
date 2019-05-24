package com.wdy.module.core.common.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: HttpResponseUtil
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-20 10:15
 */
public class HttpResponseUtil {

    public static void writeToResponse(HttpServletRequest request,
                                       HttpServletResponse response, String fileName, Integer mode) {
        try {
            String userAgent = request.getHeader("User-Agent");
            // 解决中文乱码问题
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            String newFilename = fileName + sdf.format(new Date()) + (mode == 0 ? ".ppt" : ".pdf");
            newFilename = URLEncoder.encode(newFilename, "UTF-8");
            // 如果没有userAgent，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
            String rtn = "filename=\"" + newFilename + "\"";
            if (userAgent != null) {
                userAgent = userAgent.toLowerCase();
                // IE浏览器，只能采用URLEncoder编码
                if (userAgent.indexOf("IE") != -1) {
                    rtn = "filename=\"" + newFilename + "\"";
                }
                // Opera浏览器只能采用filename*
                else if (userAgent.indexOf("OPERA") != -1) {
                    rtn = "filename*=UTF-8''" + newFilename;
                }
                // Safari浏览器，只能采用ISO编码的中文输出
                else if (userAgent.indexOf("SAFARI") != -1) {
                    rtn = "filename=\"" + new String(newFilename.getBytes("UTF-8"), "ISO-8859-1")
                            + "\"";
                }
                // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
                else if (userAgent.indexOf("FIREFOX") != -1) {
                    rtn = "filename*=UTF-8''" + newFilename;
                }
            }
            String headStr = "attachment;  " + rtn;
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", headStr);
//            response.setContentType("application/x-download");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportFile(HttpServletResponse response, byte[] file) throws IOException {
        InputStream fis = new BufferedInputStream(new ByteArrayInputStream(file));
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        byte[] buffer = new byte[1024 * 1024 * 4];
        int i;
        while ((i = fis.read(buffer)) != -1) {
            toClient.write(buffer, 0, i);
        }
        fis.close();
        toClient.flush();
        toClient.close();
    }
}