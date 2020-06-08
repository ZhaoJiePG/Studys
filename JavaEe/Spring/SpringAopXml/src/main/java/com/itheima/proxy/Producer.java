package com.itheima.proxy;

/**
 * Created by ZJ on 2020/6/8
 * comment:生产者
 */
public class Producer implements IProducer{

    /* *
     * @Description: 销售 
     * @Param: [money] 
     * @return: void 
     */
    @Override
    public void saleProduct(float money){
        System.out.println("销售产品，并拿到钱"+money);
    }

    /* *
     * @Description: 售后
     * @Param: [money] 
     * @return: void 
     */
    @Override
    public void afterService(float money){
        System.out.println("提供售后服务，并拿到钱"+money);
    }
}
