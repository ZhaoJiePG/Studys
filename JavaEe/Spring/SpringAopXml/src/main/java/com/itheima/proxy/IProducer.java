package com.itheima.proxy;

/**
 * Created by ZJ on 2020/6/8
 * comment:生产厂家的接口
 */
public interface IProducer {
    /* *
     * @Description: 销售
     * @Param: [money]
     * @return: void
     */
    public void saleProduct(float money);

    /* *
     * @Description: 售后
     * @Param: [money]
     * @return: void
     */
    public void afterService(float money);
}
