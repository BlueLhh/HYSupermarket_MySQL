package com.alan.hysupermarket.service;

import com.alan.hysupermarket.pojo.Orders;
import com.alan.hysupermarket.pojo.OrdersItem;

import java.util.List;

public interface IOrdersService {
	String waitPay = "waitPay";
	String waitDelivery = "waitDelivery";
	String waitConfirm = "waitConfirm";
	String waitReview = "waitReview";
	String finish = "finish";
	String delete = "delete";

	void add(Orders Orders);

	float add(Orders Orders, List<OrdersItem> ois);

	void delete(int id);

	void update(Orders Orders);

	Orders get(int id);

	List<Orders> list();

	List<Orders> list(int uid, String excludedStatus);
}
