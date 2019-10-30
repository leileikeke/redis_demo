package com.lk.redis_demo.service;

import com.lk.redis_demo.common.AppResponse;

/**
 * @description:
 * @author: leike
 * @date: 2019-10-29 18:58
 */
public interface RedisService {

    AppResponse insertString(String msg);

}
