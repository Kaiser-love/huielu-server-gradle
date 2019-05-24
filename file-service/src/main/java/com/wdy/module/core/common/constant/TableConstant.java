package com.wdy.module.core.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: TableConstant
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-19 15:53
 */
public class TableConstant {
    public final static String TABLE_PPT = "ppt";
    public final static String TABLE_PDF = "pdf";

    public final static String ENTITY_BASE_PACKAGE = "com.wdy.module.modular.system.entity.%s";
    public static Map<String, String> entityNameToTableName = new HashMap<>();

    static {
        entityNameToTableName.put("Ppt", TABLE_PPT);
        entityNameToTableName.put("Pdf", TABLE_PDF);
    }
}