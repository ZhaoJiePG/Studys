package redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

public class RedisUtil {
    private RedisUtil() {
    }

    public static JedisCluster getJedisCluster() {
        return RedisClusterPoolHolder.getInstance();
    }

    private static final class RedisClusterPoolHolder {
        private static final ClusterPool CLUSTER_POOL = new ClusterPool();

        private RedisClusterPoolHolder() {}

        private static JedisCluster getInstance() {
            return CLUSTER_POOL.getJedisCluster();
        }

    }

    private static class ClusterPool {
        private static final HostAndPort CLUSTER_NODE_1 = new HostAndPort("10.149.1.150", 6379);
        private static final HostAndPort CLUSTER_NODE_2 = new HostAndPort("10.149.1.150", 6380);
        private static final HostAndPort CLUSTER_NODE_3 = new HostAndPort("10.149.1.150", 6381);
        private static final HostAndPort CLUSTER_NODE_4 = new HostAndPort("10.149.1.220", 6379);
        private static final HostAndPort CLUSTER_NODE_5 = new HostAndPort("10.149.1.220", 6380);
        private static final HostAndPort CLUSTER_NODE_6 = new HostAndPort("10.149.1.220", 6381);

        private static final Set<HostAndPort> NODES = new HashSet<HostAndPort>() {
            {
                add(CLUSTER_NODE_1);
                add(CLUSTER_NODE_2);
                add(CLUSTER_NODE_3);
                add(CLUSTER_NODE_4);
                add(CLUSTER_NODE_5);
                add(CLUSTER_NODE_6);
            }
        };
        /**
         * 可用连接实例的最大数目，默认值为8； 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
         */
        private static final int MAX_ACTIVE = 1024;
        /**
         * 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
         */
        private static final int MAX_IDLE = 200;
        /**
         * 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
         */
        private static final int MAX_WAIT = 10000;
        private static final int TIMEOUT = 10000;
        /**
         * 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
         */
        private static final boolean TEST_ON_BORROW = true;
        /**
         * JedisCluster
         */
        private static JedisCluster JEDIS_CLUSTER = null;

        ClusterPool() {

            /**
             * 初始化Redis-Cluster连接池.
             */
            try {
                // maxActive ==> maxTotal
                // maxWait ==> maxWaitMillisl
                /*
                 * 配置JedisPool*/
                JedisPoolConfig CONFIG = new JedisPoolConfig();
                CONFIG.setMaxTotal(MAX_ACTIVE);
                CONFIG.setMaxIdle(MAX_IDLE);
                CONFIG.setMaxWaitMillis(MAX_WAIT);
                CONFIG.setTestOnBorrow(TEST_ON_BORROW);
                JEDIS_CLUSTER = new JedisCluster(NODES, TIMEOUT, CONFIG);
            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
            }
        }

        private JedisCluster getJedisCluster() {
            return JEDIS_CLUSTER;
        }
    }
}
