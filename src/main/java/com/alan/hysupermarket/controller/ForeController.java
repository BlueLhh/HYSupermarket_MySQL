package com.alan.hysupermarket.controller;

import com.alan.hysupermarket.pojo.*;
import com.alan.hysupermarket.service.*;
import com.alan.hysupermarket.utils.SendEmailUtils;
import com.github.pagehelper.PageHelper;
import comparator.*;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class ForeController {
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IUsersService usersService;
	@Autowired
	private IProductImageService productImageService;
	@Autowired
	private IPropertyValueService propertyValueService;
	@Autowired
	private IOrdersService ordersService;
	@Autowired
	private IOrdersItemService ordersItemService;
	@Autowired
	private IReviewService reviewService;

	@RequestMapping("forehome")
	public String home(Model model) {
		List<Category> cs = categoryService.list();
		productService.fill(cs);
		productService.fillByRow(cs);
		model.addAttribute("cs", cs);
		return "fore/home";
	}

	@RequestMapping("foreregister")
	public String register(Model model, Users user) {
		String name = user.getName();
		name = HtmlUtils.htmlEscape(name);
		user.setName(name);
		boolean exist = usersService.isExist(name);

		if (exist) {
			String m = "用户名已经被使用,不能使用";
			model.addAttribute("msg", m);
			return "fore/register";
		}
		usersService.add(user);

		return "redirect:registerSuccessPage";
	}

	@RequestMapping("forelogin")
	public String login(@RequestParam("name") String name, @RequestParam("password") String password, Model model,
			HttpSession session) {
		name = HtmlUtils.htmlEscape(name);
		Users user = usersService.get(name, password);

		if (null == user) {
			model.addAttribute("msg", "账号密码错误");
			return "fore/login";
		}
		// setAttribute
		session.setAttribute("user", user);
		return "redirect:forehome";
	}

	// 这里写的是找回密码的方法 根据用户名查找
	@RequestMapping(value = "findPassword", method = { RequestMethod.GET, RequestMethod.POST })
	public String findPassword(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("name") String name) {
		name = HtmlUtils.htmlEscape(name);
		String password = usersService.password(name);
		// 密码为空 返回找回密码界面
		if (password == null || "".equals(password)) {
			String msg = "抱歉，不存在该用户，请重新输入！";
			model.addAttribute("msg", msg);
			return "fore/forgetPassword";
		}
		// 如果存在这个账号，则发送信息
		// 邮件的内容
		StringBuffer sb = new StringBuffer("尊敬的客户您好，这是您在鸿雁超市网上购物商城的密码：" + password + "，请您牢记！");
		// 发送邮件
		SendEmailUtils.send(name, sb.toString());
		String msg = "找回密码成功，请登录邮箱获取密码！";
		model.addAttribute("msg", msg);
		return "fore/forgetPassword";
	}

	@RequestMapping("forelogout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		return "redirect:forehome";
	}

	@RequestMapping("foreproduct")
	public String product(int pid, Model model) {
		Product p = productService.get(pid);

		List<ProductImage> productSingleImages = productImageService.list(p.getId(), IProductImageService.type_single);
		List<ProductImage> productDetailImages = productImageService.list(p.getId(), IProductImageService.type_detail);
		p.setProductSingleImages(productSingleImages);
		p.setProductDetailImages(productDetailImages);

		List<PropertyValue> pvs = propertyValueService.list(p.getId());
		List<Review> reviews = reviewService.list(p.getId());
		productService.setSaleAndReviewNumber(p);

		model.addAttribute("reviews", reviews);
		model.addAttribute("p", p);
		model.addAttribute("pvs", pvs);
		return "fore/product";
	}

	@RequestMapping("forecheckLogin")
	@ResponseBody
	public String checkLogin(HttpSession session) {
		Users user = (Users) session.getAttribute("user");
		if (null != user)
			return "success";
		return "fail";
	}

	@RequestMapping("foreloginAjax")
	@ResponseBody
	public String loginAjax(@RequestParam("name") String name, @RequestParam("password") String password,
			HttpSession session) {
		name = HtmlUtils.htmlEscape(name);
		Users user = usersService.get(name, password);

		if (null == user) {
			return "fail";
		}
		session.setAttribute("user", user);
		return "success";
	}

	@RequestMapping("forecategory")
	public String category(int cid, String sort, Model model) {
		Category c = categoryService.get(cid);
		productService.fill(c);
		productService.setSaleAndReviewNumber(c.getProducts());

		if (null != sort) {
			switch (sort) {
			case "review":
				Collections.sort(c.getProducts(), new ProductReviewComparator());
				break;
			case "date":
				Collections.sort(c.getProducts(), new ProductDateComparator());
				break;

			case "saleCount":
				Collections.sort(c.getProducts(), new ProductSaleCountComparator());
				break;

			case "price":
				Collections.sort(c.getProducts(), new ProductPriceComparator());
				break;

			case "all":
				Collections.sort(c.getProducts(), new ProductAllComparator());
				break;
			}
		}

		model.addAttribute("c", c);
		return "fore/category";
	}

	@RequestMapping("foresearch")
	public String search(String keyword, Model model) {

		PageHelper.offsetPage(0, 20);
		List<Product> ps = productService.search(keyword);
		productService.setSaleAndReviewNumber(ps);
		model.addAttribute("ps", ps);
		return "fore/searchResult";
	}

	@RequestMapping("forebuyone")
	public String buyone(int pid, int num, HttpSession session) {
		Product p = productService.get(pid);

		int oiid = 0;
		Users user = (Users) session.getAttribute("user");
		boolean found = false;
		List<OrdersItem> ois = ordersItemService.listByUser(user.getId());
		// 遍历循环子订单是否有相同的，有的话即增加
		for (OrdersItem oi : ois) {
			if (oi.getProduct().getId().intValue() == p.getId().intValue()) {
				oi.setNumber(oi.getNumber() + num);
				ordersItemService.update(oi);
				found = true;
				oiid = oi.getId();
				break;
			}
		}

		if (!found) {
			OrdersItem oi = new OrdersItem();
			oi.setUid(user.getId());
			oi.setNumber(num);
			oi.setPid(pid);
			ordersItemService.add(oi);
			oiid = oi.getId();
		}
		return "redirect:forebuy?oiid=" + oiid;
	}

	@RequestMapping("forebuy")
	public String buy(Model model, String[] oiid, HttpSession session) {
		List<OrdersItem> ois = new ArrayList<>();
		float total = 0;

		for (String strid : oiid) {
			int id = Integer.parseInt(strid);
			OrdersItem oi = ordersItemService.get(id);
			total += oi.getProduct().getPromotePrice() * oi.getNumber();
			ois.add(oi);
		}

		session.setAttribute("ois", ois);
		model.addAttribute("total", total);
		return "fore/buy";
	}

	@RequestMapping("foreaddCart")
	@ResponseBody
	public String addCart(int pid, int num, Model model, HttpSession session) {
		Product p = productService.get(pid);
		if (p.getStock() <= 0) {
			return "fail";
		}
		Users user = (Users) session.getAttribute("user");
		boolean found = false;

		List<OrdersItem> ois = ordersItemService.listByUser(user.getId());
		for (OrdersItem oi : ois) {
			if (oi.getProduct().getId().intValue() == p.getId().intValue()) {
				oi.setNumber(oi.getNumber() + num);
				ordersItemService.update(oi);
				found = true;
				break;
			}
		}

		if (!found) {
			OrdersItem oi = new OrdersItem();
			oi.setUid(user.getId());
			oi.setNumber(num);
			oi.setPid(pid);
			ordersItemService.add(oi);
		}
		return "success";
	}

	@RequestMapping("forecart")
	public String cart(Model model, HttpSession session) {
		Users user = (Users) session.getAttribute("user");
		List<OrdersItem> ois = ordersItemService.listByUser(user.getId());
		model.addAttribute("ois", ois);
		return "fore/cart";
	}

	@RequestMapping("forechangeOrderItem")
	@ResponseBody
	public String changeOrderItem(Model model, HttpSession session, int pid, int number) {
		Users user = (Users) session.getAttribute("user");
		if (null == user)
			return "fail";

		List<OrdersItem> ois = ordersItemService.listByUser(user.getId());
		for (OrdersItem oi : ois) {
			if (oi.getProduct().getId().intValue() == pid) {
				oi.setNumber(number);
				ordersItemService.update(oi);
				break;
			}

		}
		return "success";
	}

	@RequestMapping("foredeleteOrderItem")
	@ResponseBody
	public String deleteOrderItem(Model model, HttpSession session, int oiid) {
		Users user = (Users) session.getAttribute("user");
		if (null == user)
			return "fail";
		ordersItemService.delete(oiid);
		return "success";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("forecreateOrder")
	public String createOrder(Model model, Orders order, HttpSession session, HttpServletResponse response)
			throws IOException {
		Users user = (Users) session.getAttribute("user");
		String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
		order.setOrderCode(orderCode);
		order.setCreateDate(new Date());
		order.setUid(user.getId());
		order.setStatus(IOrdersService.waitPay);
		List<OrdersItem> ois = (List<OrdersItem>) session.getAttribute("ois");
		// 生成订单后，库存减少
		for (OrdersItem ordersItem : ois) {
			// 获取商品的信息
			Product p = new Product();
			p = productService.get(ordersItem.getPid());
			// 获取商品的库存-购买的商品数量
			if (p.getStock() >= ordersItem.getNumber()) {
				int stock = p.getStock() - ordersItem.getNumber();
				// 更新商品的数量
				p.setStock(stock);
				// 更新商品的库存
				productService.update(p);
			} else {
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(
						"<script>javascript:alert('抱歉，您购买的"+p.getName()+"库存不足！');location.href='/WEB-INF/jsp/fore/buy.jsp'</script>");
				return "redirect:forebuy?oiid=" + ordersItem.getId();
			}

		}

		float total = ordersService.add(order, ois);
		return "redirect:forealipay?oid=" + order.getId() + "&total=" + total;
	}

	@RequestMapping("forepayed")
	public String payed(int oid, float total, Model model) {
		Orders order = ordersService.get(oid);
		order.setStatus(IOrdersService.waitDelivery);
		order.setPayDate(new Date());
		ordersService.update(order);
		model.addAttribute("o", order);
		return "fore/payed";
	}

	@RequestMapping("forebought")
	public String bought(Model model, HttpSession session) {
		Users user = (Users) session.getAttribute("user");
		List<Orders> os = ordersService.list(user.getId(), IOrdersService.delete);

		ordersItemService.fill(os);

		model.addAttribute("os", os);

		return "fore/bought";
	}

	@RequestMapping("foreconfirmPay")
	public String confirmPay(Model model, int oid) {
		Orders o = ordersService.get(oid);
		ordersItemService.fill(o);
		model.addAttribute("o", o);
		return "fore/confirmPay";
	}

	@RequestMapping("foreorderConfirmed")
	public String orderConfirmed(Model model, int oid) {
		Orders o = ordersService.get(oid);
		o.setStatus(IOrdersService.waitReview);
		o.setConfirmDate(new Date());
		ordersService.update(o);
		return "fore/orderConfirmed";
	}

	@RequestMapping("foredeleteOrder")
	@ResponseBody
	public String deleteOrder(Model model, int oid) {
		Orders o = ordersService.get(oid);
		o.setStatus(IOrdersService.delete);
		ordersService.update(o);
		return "success";
	}

	@RequestMapping("forereview")
	public String review(Model model, int oid) {
		Orders o = ordersService.get(oid);
		ordersItemService.fill(o);
		Product p = o.getOrderItems().get(0).getProduct();
		List<Review> reviews = reviewService.list(p.getId());
		productService.setSaleAndReviewNumber(p);
		model.addAttribute("p", p);
		model.addAttribute("o", o);
		model.addAttribute("reviews", reviews);
		return "fore/review";
	}

	@RequestMapping("foredoreview")
	public String doreview(Model model, HttpSession session, @RequestParam("oid") int oid, @RequestParam("pid") int pid,
			String content) {
		Orders o = ordersService.get(oid);
		o.setStatus(IOrdersService.finish);
		ordersService.update(o);

		@SuppressWarnings("unused")
		Product p = productService.get(pid);
		content = HtmlUtils.htmlEscape(content);

		Users user = (Users) session.getAttribute("user");
		Review review = new Review();
		review.setContent(content);
		review.setPid(pid);
		review.setCreateDate(new Date());
		review.setUid(user.getId());
		reviewService.add(review);

		return "redirect:forereview?oid=" + oid + "&showonly=true";
	}

}
