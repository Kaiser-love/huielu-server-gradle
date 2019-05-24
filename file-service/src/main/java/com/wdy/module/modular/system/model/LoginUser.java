package com.wdy.module.modular.system.model;

import lombok.*;

/**
 * @program: guns
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-19 13:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LoginUser {
    private String username;
    private String password;
}