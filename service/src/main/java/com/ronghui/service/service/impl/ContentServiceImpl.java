package com.ronghui.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ronghui.service.repository.ContentRepository;
import com.ronghui.service.entity.Content;
import com.ronghui.service.service.ContentService;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRespository;

    @Autowired
    public ContentServiceImpl(ContentRepository contentRespository) {
        this.contentRespository = contentRespository;
    }


    public Content findByName(String name) {
        return contentRespository.findByName(name);
    }

    public void save(Content content) {
        contentRespository.save(content);
    }

    public void delete(long cid) {
        contentRespository.deleteById(cid);
    }

    @Override
    public List<Content> findByType(String type) {
        if (StringUtils.isEmpty(type)) {
            return contentRespository.findContentsByTypeOrType(Content.ContextType.All, Content.ContextType.All);
        }
        if (type.equals(Content.ContextType.Android.name())) {
            return contentRespository.findContentsByTypeOrType(Content.ContextType.Android, Content.ContextType.All);
        } else if (type.equals(Content.ContextType.Web.name())) {
            return contentRespository.findContentsByTypeOrType(Content.ContextType.Web, Content.ContextType.All);
        } else if (type.equals(Content.ContextType.WeChat.name())) {
            return contentRespository.findContentsByTypeOrType(Content.ContextType.WeChat, Content.ContextType.All);
        } else {
            return contentRespository.findContentsByTypeOrType(Content.ContextType.All, Content.ContextType.All);
        }
    }


}
