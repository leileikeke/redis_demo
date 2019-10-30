package com.lk.redis_demo.service.impl;

import com.lk.redis_demo.common.AppResponse;
import com.lk.redis_demo.dao.RedisDao;
import com.lk.redis_demo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: leike
 * @date: 2019-10-29 19:00
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisDao redisDao;

    @Override
    public AppResponse insertString(String msg) {

        try {
            int i = redisDao.insertString(msg);
            if (i > 0) {
                return AppResponse.success("插入成功");
            }else {
                return AppResponse.notFound("插入失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.bizError("插入失败,请联系管理员");
        }

    }
}
