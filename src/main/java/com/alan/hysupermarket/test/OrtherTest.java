package com.alan.hysupermarket.test;

import com.alan.hysupermarket.mapper.CategoryMapper;
import com.alan.hysupermarket.pojo.Category;

public class OrtherTest {

    public static void main(String[] args) {
        CategoryMapper categoryMapper = null;
       // CategoryExample example = new CategoryExample();
//        example.setOrderByClause("id desc");
//
//        List<Category> categories = categoryMapper.selectByExample(example);
//
//        for (Category category:categories) {
//            System.out.println(category.getNAME=());
//        }

        @SuppressWarnings("null")
		Category category = categoryMapper.selectByPrimaryKey(60);

        System.out.println(category.getName());
    }

}
