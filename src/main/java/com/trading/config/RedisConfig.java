package com.trading.config;

import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisConfig {

   private RedisConfig() {}
   private static final RedissonClient redissonClient = null;

   public static RedissonClient getRedissonClient() {
       Config config = new Config();
       config.useClusterServers()
               .addNodeAddress(
                       "redis://127.0.0.1:7001",
                       "redis://127.0.0.1:7002",
                       "redis://127.0.0.1:7003"
               )
               .setScanInterval(2000);
       return redissonClient;
   }
}