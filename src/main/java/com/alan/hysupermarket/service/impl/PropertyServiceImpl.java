package com.alan.hysupermarket.service.impl;

import com.alan.hysupermarket.mapper.PropertyMapper;
import com.alan.hysupermarket.pojo.Property;
import com.alan.hysupermarket.pojo.PropertyExample;
import com.alan.hysupermarket.service.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyServiceImpl implements IPropertyService {

    @Autowired
    private PropertyMapper propertyMapper;

    @Override
    public void add(Property property) {
        propertyMapper.insert(property);
    }

    @Override
    public void delete(int id) {
        propertyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Property property) {
        propertyMapper.updateByPrimaryKeySelective(property);
    }

    @Override
    public Property get(int id) {
        return propertyMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Property> list(int cid) {
        PropertyExample example = new PropertyExample();
        example.createCriteria().andCidEqualTo(cid);
        example.setOrderByClause("id desc");
        return propertyMapper.selectByExample(example);
    }
}
