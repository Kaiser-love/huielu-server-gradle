package com.wdy.module.modular.system.service;


import com.wdy.module.core.common.response.ResponseHelper;
import com.wdy.module.core.common.constant.TableConstant;
import com.wdy.module.core.common.exception.ResultEnum;
import com.wdy.module.core.common.request.QueryAllBean;
import com.wdy.module.core.common.response.ResultBean;
import com.wdy.module.core.common.utils.ConditionUtil;
import com.wdy.module.modular.system.jpa.BaseDao;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: guns
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-19 15:48
 */
@Service
public class BaseService {
    @Autowired
    private BaseDao baseDao;

    public ResponseEntity<ResultBean> getEntityList(QueryAllBean queryAllBean) throws Exception {
        String result = queryAllBean.getResult(), entityName = queryAllBean.getEntityName();
        if (result == null)
            throw new ServiceException(ResultEnum.QUERY_LIST_ARGS_ERROR);
        String tableName = TableConstant.entityNameToTableName.get(entityName);
        Class clazz = Class.forName(String.format(TableConstant.ENTITY_BASE_PACKAGE, entityName));
        List resultList = null;
        List list = null;
        String query = queryAllBean.getQuery(), queryString = queryAllBean.getQueryString();
        Integer page = queryAllBean.getPage(), count = queryAllBean.getPagecount();
        // 带条件或查询
        if (query != null && query.contains(" ")) {
            resultList = baseDao.findAllBySql(tableName, "like", query, queryString, page, count, clazz);
        }
        // 查询全部
        if (result.equals(ConditionUtil.QUERY_ALL)) {
            resultList = baseDao.findAll(tableName, clazz);
        }
        // 查询全部分页
        if (result.equals(ConditionUtil.QUERY_ALL_PAGE)) {
            resultList = baseDao.findAllByPage(tableName, page, count, clazz);
        }
        // 带条件查询全部
        if (result.equals(ConditionUtil.QUERY_ATTRIBUTE_ALL)) {
            list = baseDao.findAll(tableName, clazz);
            resultList = baseDao.findAllBySql(tableName, query, queryString, clazz);
        }
        // 带条件查询分页
        if (result.equals(ConditionUtil.QUERY_ATTRIBUTE_PAGE)) {
            list = baseDao.findAll(tableName, clazz);
            resultList = baseDao.findAllBySql(tableName, query, queryString, page, count, clazz);
        }
        if (resultList == null)
            return ResponseHelper.BadRequest("查询组合出错 函数未执行！");
        return ResponseHelper.OK(resultList, list != null ? list.size() : resultList.size());

    }

}