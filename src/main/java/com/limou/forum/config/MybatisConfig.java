package com.limou.forum.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 小李哞哞
 * @date 2023/8/26
 */
@Configuration
@MapperScan("com.limou.forum.dao")
public class MybatisConfig {

}
