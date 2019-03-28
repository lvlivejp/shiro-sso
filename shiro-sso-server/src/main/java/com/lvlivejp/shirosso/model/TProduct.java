package com.lvlivejp.shirosso.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`t_product`")
public class TProduct {
    /**
     * 商品编码
     */
    @Id
    @Column(name = "product_code")
    //通用mapper中通过该注解标记用来获取自增主键的字段
    @GeneratedValue(generator = "JDBC")
    private Integer productCode;

    /**
     * 商品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 开始时间
     */
    @Column(name = "start_date")
    private Date startDate;

    /**
     * 结束时间
     */
    @Column(name = "end_date")
    private Date endDate;

    /**
     * 库存
     */
    private Integer count;

    /**
     * 获取商品编码
     *
     * @return product_code - 商品编码
     */
    public Integer getProductCode() {
        return productCode;
    }

    /**
     * 设置商品编码
     *
     * @param productCode 商品编码
     */
    public void setProductCode(Integer productCode) {
        this.productCode = productCode;
    }

    /**
     * 获取商品名称
     *
     * @return product_name - 商品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置商品名称
     *
     * @param productName 商品名称
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取开始时间
     *
     * @return start_date - 开始时间
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * 设置开始时间
     *
     * @param startDate 开始时间
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 获取结束时间
     *
     * @return end_date - 结束时间
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * 设置结束时间
     *
     * @param endDate 结束时间
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取库存
     *
     * @return count - 库存
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置库存
     *
     * @param count 库存
     */
    public void setCount(Integer count) {
        this.count = count;
    }
}