package com.alan.hysupermarket.service.impl;

import com.alan.hysupermarket.mapper.PropertyValueMapper;
import com.alan.hysupermarket.pojo.Product;
import com.alan.hysupermarket.pojo.Property;
import com.alan.hysupermarket.pojo.PropertyValue;
import com.alan.hysupermarket.pojo.PropertyValueExample;
import com.alan.hysupermarket.service.IPropertyService;
import com.alan.hysupermarket.service.IPropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyValueServiceImpl implements IPropertyValueService {

	@Autowired
	private PropertyValueMapper propertyValueMapper;

	@Autowired
	private IPropertyService propertyService;

	@Override
	public void init(Product product) {
		List<Property> propertyList = propertyService.list(product.getCid());

		for (Property property : propertyList) {
			PropertyValue propertyValue = get(property.getId(), product.getId());
			if (null == propertyValue) {
				propertyValue = new PropertyValue();
				propertyValue.setPid(product.getId());
				propertyValue.setPid(property.getId());
				propertyValueMapper.insert(propertyValue);
			}
		}

	}

	@Override
	public void update(PropertyValue propertyValue) {
		propertyValueMapper.updateByPrimaryKeySelective(propertyValue);
	}

	@Override
	public PropertyValue get(int ptid, int pid) {
		PropertyValueExample example = new PropertyValueExample();
		example.createCriteria().andPtidEqualTo(ptid).andPidEqualTo(pid);
		List<PropertyValue> pvs = propertyValueMapper.selectByExample(example);
		if (pvs.isEmpty()) {
			return null;
		}
		return pvs.get(0);
	}

	@Override
	public List<PropertyValue> list(int pid) {
		PropertyValueExample example = new PropertyValueExample();
		example.createCriteria().andPidEqualTo(pid);
		List<PropertyValue> result = propertyValueMapper.selectByExample(example);
		for (PropertyValue pv : result) {
			Property property = propertyService.get(pv.getPtid());
			pv.setProperty(property);
		}
		return result;
	}
}
