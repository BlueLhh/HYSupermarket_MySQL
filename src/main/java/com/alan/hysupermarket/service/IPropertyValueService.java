package com.alan.hysupermarket.service;

import com.alan.hysupermarket.pojo.Product;
import com.alan.hysupermarket.pojo.PropertyValue;

import java.util.List;

public interface IPropertyValueService {

    public void init(Product product);

    public void update(PropertyValue propertyValue);

    public PropertyValue get(int ptid, int pid);

    public List<PropertyValue> list(int pid);

}
