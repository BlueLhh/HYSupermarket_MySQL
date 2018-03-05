package com.alan.hysupermarket.service;


import com.alan.hysupermarket.pojo.Orders;
import com.alan.hysupermarket.pojo.OrdersItem;

import java.util.List;

public interface IOrdersItemService {
    void add(OrdersItem OrdersItem);

    void delete(int id);

    void update(OrdersItem OrdersItem);

    OrdersItem get(int id);

    List<OrdersItem> list();

    void fill(List<Orders> Orders);

    void fill(Orders Orders);

    int getSaleCount(int pid);

    List<OrdersItem> listByUser(int uid);
}
