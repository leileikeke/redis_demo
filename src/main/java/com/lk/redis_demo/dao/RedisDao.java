package com.lk.redis_demo.dao;

import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: leike
 * @date: 2019-10-29 18:58
 */
@Repository
public interface RedisDao {

    int insertString(String msg);

}
