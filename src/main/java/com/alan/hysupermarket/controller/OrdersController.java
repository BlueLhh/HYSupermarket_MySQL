package com.alan.hysupermarket.controller;

import com.alan.hysupermarket.pojo.Orders;
import com.alan.hysupermarket.pojo.Users;
import com.alan.hysupermarket.service.IOrdersItemService;
import com.alan.hysupermarket.service.IOrdersService;
import com.alan.hysupermarket.service.IUsersService;
import com.alan.hysupermarket.utils.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class OrdersController {
	@Autowired
	private IOrdersService ordersService;
	@Autowired
	private IOrdersItemService ordersItemService;
	@Autowired
	private IUsersService usersService;

	@RequestMapping("admin_order_list")
	public String list(Model model, Page page) {
		PageHelper.offsetPage(page.getStart(), page.getCount());
		Users user = new Users();

		List<Orders> os = ordersService.list();
		List<Orders> list = new ArrayList<Orders>();
		for (Orders orders : os) {
			user = usersService.get(orders.getUid());
			orders.setUser(user);
			list.add(orders);
		}
		// 获取订单数
		int total = (int) new PageInfo<>(os).getTotal();
		page.setTotal(total);
		ordersItemService.fill(list);
		model.addAttribute("os", list);
		model.addAttribute("page", page);
		return "admin/listOrder";
	}

	@RequestMapping("admin_order_delivery")
	public String delivery(Orders o) throws IOException {
		o.setDeliveryDate(new Date());
		o.setStatus(IOrdersService.waitConfirm);
		ordersService.update(o);
		return "redirect:admin_order_list";
	}
}