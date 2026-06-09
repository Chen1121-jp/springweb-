package com.digital.mall.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digital.mall.item.domain.po.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ItemMapper extends BaseMapper<Item> {

    @Select("SELECT DISTINCT brand FROM item.item WHERE brand IS NOT NULL ORDER BY brand")
    List<String> selectDistinctBrands();
}
