package com.lvlivejp.shirosso.mapper;

import com.lvlivejp.shirosso.model.TProduct;

import java.util.List;

/**
 * 通用 Mapper 代码生成器
 *
 * @author mapper-generator
 */
public interface TProductDao extends tk.mybatis.mapper.common.Mapper<TProduct> {

    Long selectAll_COUNT_LV();

    public List<TProduct> countTest();

    int buy(String productCode);
}




