package com.alan.hysupermarket.service.impl;

import com.alan.hysupermarket.mapper.ReviewMapper;
import com.alan.hysupermarket.pojo.Review;
import com.alan.hysupermarket.pojo.ReviewExample;
import com.alan.hysupermarket.pojo.Users;
import com.alan.hysupermarket.service.IReviewService;
import com.alan.hysupermarket.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements IReviewService {

	@Autowired
	private ReviewMapper reviewMapper;

	@Autowired
	private IUsersService usersService;

	@Override
	public void add(Review review) {
		reviewMapper.insert(review);
	}

	@Override
	public void delete(int id) {
		reviewMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Review review) {
		reviewMapper.updateByPrimaryKeySelective(review);
	}

	@Override
	public Review get(int id) {
		return reviewMapper.selectByPrimaryKey(id);
	}

	public List<Review> list(int pid) {
		ReviewExample example = new ReviewExample();
		example.createCriteria().andPidEqualTo(pid);
		example.setOrderByClause("id desc");

		List<Review> result = reviewMapper.selectByExample(example);
		setUser(result);
		return result;
	}

	public void setUser(List<Review> reviews) {
		for (Review review : reviews) {
			setUser(review);
		}
	}

	private void setUser(Review review) {
		int uid = review.getUid();
		Users user = usersService.get(uid);
		review.setUser(user);
	}

	@Override
	public int getCount(int pid) {
		return list(pid).size();
	}
}
