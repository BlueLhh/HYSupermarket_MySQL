package com.alan.hysupermarket.mapper;

import com.alan.hysupermarket.pojo.Orders;
import com.alan.hysupermarket.pojo.OrdersExample;
import java.util.List;

public interface OrdersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Orders record);

    int insertSelective(Orders record);

    List<Orders> selectByExample(OrdersExample example);

    Orders selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);
}