package test;

import domain.Customer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.JpaUtil;

import javax.persistence.*;
import java.util.List;

/**
 * Jpa的crud
 */
public class JpaCrud {

    /**
     * 测试jpa的保存
     *      案例：保存一个客户到数据库中
     *  Jpa的操作步骤
     *     1.加载配置文件创建工厂（实体管理器工厂）对象
     *     2.通过实体管理器工厂获取实体管理器
     *     3.获取事务对象，开启事务
     *     4.完成增删改查操作
     *     5.提交事务（回滚事务）
     *     6.释放资源
     */
    /**
     * 保存一个实体
     */
    @Test
    public void testAdd() {
        // 定义对象
        Customer c = new Customer();
        c.setCustName("传智学院");
        c.setCustLevel("VIP客户");
        c.setCustSource("网络");
        c.setCustIndustry("IT教育");
        c.setCustAddress("昌平区北七家镇");
        c.setCustPhone("010-84389340");
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            // 获取实体管理对象
            em = JpaUtil.getEntityManager();
            // 获取事务对象
            tx = em.getTransaction();
            // 开启事务
            tx.begin();
            // 执行操作
            em.persist(c);
            // 提交事务
            tx.commit();
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }

    @Test
    public void testMerge(){
        //定义对象
        EntityManager em=null;
        EntityTransaction tx=null;
        try{
            //获取实体管理对象
            em=JpaUtil.getEntityManager();
            //获取事务对象
            tx=em.getTransaction();
            //开启事务
            tx.begin();
            //执行操作
            Customer c1 = em.find(Customer.class, 1L);
            c1.setCustName("江苏传智学院");
            em.clear();//把c1对象从缓存中清除出去
            em.merge(c1);
            //提交事务
            tx.commit();
        }catch(Exception e){
            //回滚事务
            tx.rollback();
            e.printStackTrace();
        }finally{
            //释放资源
            em.close();
        }
    }

    /**
     * 删除
     */
    @Test
    public void testRemove(){
        // 定义对象
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            // 获取实体管理对象
            em = JpaUtil.getEntityManager();
            // 获取事务对象
            tx = em.getTransaction();
            // 开启事务
            tx.begin();
            // 执行操作
            Customer c1 = em.find(Customer.class, 1L);
            em.remove(c1);
            // 提交事务
            tx.commit();
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }

    /**
     * 查询一个： 使用立即加载的策略
     */
    @Test
    public void testGetOne() {
        // 定义对象
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            // 获取实体管理对象
            em = JpaUtil.getEntityManager();
            // 获取事务对象
            tx = em.getTransaction();
            // 开启事务
            tx.begin();
            // 执行操作
            Customer c1 = em.find(Customer.class, 2L);
            // 提交事务
            tx.commit();
            System.out.println(c1); // 输出查询对象
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }

    //查询所有客户
    @Test
    public void findAll() {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            //获取实体管理对象
            em = JpaUtil.getEntityManager();
            //获取事务对象
            tx = em.getTransaction();
            tx.begin();
            // 创建query对象
            String jpql = "from Customer";
            Query query = em.createQuery(jpql);
            // 查询并得到返回结果
            List list = query.getResultList(); // 得到集合返回类型
            for (Object object : list) {
                System.out.println(object);
            }
            tx.commit();
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }

    /**
     * 查询一个： 使用立即加载的策略
     */
    @Test
    public void testGetOne1() {
        // 定义对象
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            // 获取实体管理对象
            em = JpaUtil.getEntityManager();
            // 获取事务对象
            tx = em.getTransaction();
            // 开启事务
            tx.begin();
            // 执行操作
            Customer c1 = em.find(Customer.class, 1L);
            // 提交事务
            tx.commit();
            System.out.println(c1); // 输出查询对象
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }

    // 查询实体的缓存问题
    @Test
    public void testGetOne2() {
        // 定义对象
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            // 获取实体管理对象
            em = JpaUtil.getEntityManager();
            // 获取事务对象
            tx = em.getTransaction();
            // 开启事务
            tx.begin();
            // 执行操作
            Customer c1 = em.find(Customer.class, 1L);
            Customer c2 = em.find(Customer.class, 1L);
            System.out.println(c1 == c2);// 输出结果是true，EntityManager也有缓存
            // 提交事务
            tx.commit();
            System.out.println(c1);
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }

    // 延迟加载策略的方法：
    /**
     * 查询一个： 使用延迟加载策略
     */
    @Test
    public void testLoadOne() {
        // 定义对象
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            // 获取实体管理对象
            em = JpaUtil.getEntityManager();
            // 获取事务对象
            tx = em.getTransaction();
            // 开启事务
            tx.begin();
            // 执行操作
            Customer c1 = em.getReference(Customer.class, 1L);
            // 提交事务
            tx.commit();
            System.out.println(c1);
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }

    //分页查询客户
    @Test
    public void findPaged () {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            //获取实体管理对象
            em = JpaUtil.getEntityManager();
            //获取事务对象
            tx = em.getTransaction();
            tx.begin();

            //创建query对象
            String jpql = "from Customer";
            Query query = em.createQuery(jpql);
            //起始索引
            query.setFirstResult(0);
            //每页显示条数
            query.setMaxResults(2);
            //查询并得到返回结果
            List list = query.getResultList(); //得到集合返回类型
            for (Object object : list) {
                System.out.println(object);
            }
            tx.commit();
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }

    //条件查询
    @Test
    public void findCondition () {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            //获取实体管理对象
            em = JpaUtil.getEntityManager();
            //获取事务对象
            tx = em.getTransaction();
            tx.begin();
            //创建query对象
            String jpql = "from Customer where custName like ? ";
            Query query = em.createQuery(jpql);
            //对占位符赋值，从1开始
            query.setParameter('2', "传智播客%");
            //查询并得到返回结果
            Object object = query.getSingleResult(); //得到唯一的结果集对象
            System.out.println(object);
            tx.commit();
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }

    //根据客户id倒序查询所有客户
    //查询所有客户
    @Test
    public void testOrder() {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            //获取实体管理对象
            em = JpaUtil.getEntityManager();
            //获取事务对象
            tx = em.getTransaction();
            tx.begin();
            // 创建query对象
            String jpql = "from Customer order by custId desc";
            Query query = em.createQuery(jpql);
            // 查询并得到返回结果
            List list = query.getResultList(); // 得到集合返回类型
            for (Object object : list) {
                System.out.println(object);
            }
            tx.commit();
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }

    //统计查询
    @Test
    public void findCount() {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            //获取实体管理对象
            em = JpaUtil.getEntityManager();
            //获取事务对象
            tx = em.getTransaction();
            tx.begin();
            // 查询全部客户
            // 1.创建query对象
            String jpql = "select count(custId) from Customer";
            Query query = em.createQuery(jpql);
            // 2.查询并得到返回结果
            Object count = query.getSingleResult(); // 得到集合返回类型
            System.out.println(count);
            tx.commit();
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }
}
