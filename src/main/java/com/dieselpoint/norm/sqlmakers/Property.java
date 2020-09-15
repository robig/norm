package com.dieselpoint.norm.sqlmakers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.EnumType;

import com.dieselpoint.norm.serialize.DbSerializable;

@SuppressWarnings("rawtypes")
public class Property {
	public String name;
	public Method readMethod;
	public Method writeMethod;
	public Field field;
	public Class<?> dataType;
    public boolean isGenerated; // means auto increment
	public boolean isPrimaryKey;
	public boolean isEnumField;
    public boolean isRelation;
    public boolean hasColumn = true;
    public Class<?> relationClass;
	public Class<Enum> enumClass;
	public EnumType enumType;
	public Column columnAnnotation;
	public DbSerializable serializer;
	public AttributeConverter converter;

    @Override
    public String toString() {
	return "Property " + name;
    }
}
