package com.alan.hysupermarket.service;

import com.alan.hysupermarket.pojo.Property;

import java.util.List;

public interface IPropertyService {

    public void add(Property property);

    public void delete(int id);

    public void update(Property property);

    public Property get(int id);

    public List<Property> list(int cid);

}
