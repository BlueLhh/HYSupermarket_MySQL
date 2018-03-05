package com.alan.hysupermarket.service;

import com.alan.hysupermarket.pojo.Review;

import java.util.List;

public interface IReviewService {

    public void add(Review review);

    public void delete(int id);

    public void update(Review review);

    public Review get(int id);

    public List<Review> list(int pid);

    public int getCount(int pid);

}
