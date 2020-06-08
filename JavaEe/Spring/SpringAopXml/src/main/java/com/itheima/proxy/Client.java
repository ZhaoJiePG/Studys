package com.itheima.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by ZJ on 2020/6/8
 * comment:
 */
public class Client {
    public static void main(String[] args) {
        final Producer producer = new Producer();
        /**
         * 动态代理：
         *  特点：字节码随用随创建，随有随加载
         *  作用：不修改源码的基础上对方法加强
         *  分类：
         *      基于接口的动态代理
         *      基于子类的动态代理
         *  基于接口的动态代理：
         *      设计的类：Proxy
         *      提供者JDK官方
         *  如何创建代理对象：
         *      使用Proxy类中的newProxyInstance方法
         *  创建代理对象的要求：
         *      被代理类最少实现一个接口，如果没有则不能使用
         *  newProxyInstance方法的参数：
         *      ClassLoader:类加载器
         *          它是用于加载代理对象字节码的，和被代理对象使用相同的类加载器
         *      Class[]：字节码数组
         *          它是用于让代理对象和倍代理对象有相同的方法。固定写法
         *      InvocationHandler
         *          它是让我们写如何代理，写该接口实现类，通常是匿名内部类
         *          此接口的实现类就是谁用谁写
         */
        IProducer proxyProducer = (IProducer)Proxy.newProxyInstance(producer.getClass().getClassLoader(),
                producer.getClass().getInterfaces(),
                new InvocationHandler() {
                    /* *
                     * @Description: 执行被代理对象的任何接口发热方法都会经过该方法
                     * @Param:
                     *  proxy   代理对象的引用
                     *  method  当前执行的方法
                     *  args    方法所需的参数
                     * @return: java.lang.Object
                     */
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //提供增强的代码
                        Object returnValue = null;
                        //1.获取方法的执行参数
                        Float money = (Float)args[0];
                        //2.判断当前方法是不是销售
                        if("saleProduct".equals(method.getName()))
                        returnValue = method.invoke(producer,money*0.8f);
                        return returnValue;
                    }
                });

        producer.saleProduct(10000f);
    }
}
