package com.wdy.module.modular.system.service;

import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.wdy.module.core.common.response.ResponseHelper;
import com.wdy.module.core.common.exception.ResultEnum;
import com.wdy.module.core.common.response.SuccessAndErrorList;
import com.wdy.module.core.common.utils.TimeUtil;
import com.wdy.module.core.common.utils.ZimgServiceUtil;
import com.wdy.module.core.shiro.ShiroKit;
import com.wdy.module.core.shiro.ShiroUser;
import com.wdy.module.modular.system.entity.Pdf;
import com.wdy.module.modular.system.entity.Ppt;
import com.wdy.module.modular.system.mapper.PptMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.wdy.module.core.common.constant.ControllerModeConstant.*;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wdy
 * @since 2019-05-18
 */
@Service("PptService")
public class PptService extends ServiceImpl<PptMapper, Ppt> {
    @Autowired
    private ZimgServiceUtil zimgServiceUtil;
    @Autowired
    private FileService fileService;

    public SuccessAndErrorList uploadPicture2PPTOrPDF(List<MultipartFile> files, String name, Integer mode) throws Exception {
        List<String> resultList = new ArrayList<>();
        List<String> successResultList = new ArrayList<>();
        List<String> errorResultList = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String apiResult = zimgServiceUtil.sendPost(file);
            if (!apiResult.equals(ResponseHelper.OPERATION_BADREQUEST)) {
                successResultList.add(fileName);
                resultList.add(apiResult);
            } else
                errorResultList.add(fileName);
        }
        Object obj = savePPTOrPDF(name, resultList, mode);
        return SuccessAndErrorList.builder().successResultList(successResultList).errorResultList(errorResultList).object(obj).build();
    }

    public Object savePPTOrPDF(String name, List<String> imgs, Integer mode) throws Exception {
        ShiroUser user = ShiroKit.getUser();
        switch (mode) {
            case PPTCONTROLLER_DO_PPT:
                String pptID = fileService.createPPTWithImgUrl(name, imgs);
                if (!StringUtils.isEmpty(pptID)) {
                    Ppt ppt = Ppt.builder()
                            .uid(user.getId()).name(name).datetime(TimeUtil.currentTimeStamp()).cover(imgs.get(0)).fileId(pptID).build();
                    ppt.insert();
                    return ppt;
                }
            case PPTCONTROLLER_DO_PDF:
                String pdfid = fileService.createPDFWithImgUrl(name, imgs);
                if (!StringUtils.isEmpty(pdfid)) {
                    Pdf pdf = Pdf.builder()
                            .uid(user.getId()).name(name).datetime(TimeUtil.currentTimeStamp()).cover(imgs.get(0)).fileId(pdfid).build();
                    pdf.insert();
                    return pdf;
                }
            default:
                break;
        }
        throw new ServiceException(ResultEnum.MODE_ERROR_CREATE_PPT_OR_PDF);
    }
}
