package com.lvlivejp.shirosso.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`t_buy_info`")
public class TBuyInfo {
    @Id
    @Column(name = "product_code")
    private String productCode;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "buy_date")
    private Date buyDate;

    private String state;

    /**
     * @return product_code
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * @param productCode
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return buy_date
     */
    public Date getBuyDate() {
        return buyDate;
    }

    /**
     * @param buyDate
     */
    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    /**
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }
}