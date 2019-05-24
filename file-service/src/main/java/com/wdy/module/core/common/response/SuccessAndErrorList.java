package com.wdy.module.core.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * SuccessAndErrorList
 *
 * @author dongyang_wu
 * @date 2019/5/21 10:57
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SuccessAndErrorList {
    private List<String> successResultList;
    private List<String> errorResultList;
    private Object object;
}
