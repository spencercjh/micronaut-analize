package com.example.jooq.converter;

import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.impl.SQLDataType;
import org.jooq.tools.StringUtils;

import javax.inject.Singleton;

/**
 * @author spencercjh
 */
@Singleton
public class MultivaluedStringToArrayConverter implements Converter<String, String[]> {
  public static final DataType<String[]> STRING_ARRAY_TYPE = SQLDataType.VARCHAR.asConvertedDataType(
    new MultivaluedStringToArrayConverter());

  @Override
  public String[] from(String databaseObject) {
    if (StringUtils.isEmpty(databaseObject)) {
      return new String[]{};
    }
    return databaseObject.split(",");
  }

  @Override
  public String to(String[] userObject) {
    if (userObject == null || userObject.length == 0) {
      return "";
    }
    return String.join(",", userObject);
  }

  @Override
  public Class<String> fromType() {
    return String.class;
  }

  @Override
  public Class<String[]> toType() {
    return String[].class;
  }
}
