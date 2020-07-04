package com.example.jooq.converter;

import ai.inceptio.chariot.v1alpha2.resources.ReleaseType;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.impl.SQLDataType;

import javax.inject.Singleton;

/**
 * @author Jiahao Cai
 */
@Singleton
public class ReleaseTypeConverter implements Converter<String, ReleaseType> {
  public static final DataType<ReleaseType> RELEASE_TYPE_DATA_TYPE = SQLDataType.VARCHAR.asConvertedDataType(new ReleaseTypeConverter());

  @Override
  public ReleaseType from(String name) {
    return ReleaseType.valueOf(name);
  }

  @Override
  public String to(ReleaseType releaseType) {
    return releaseType != null ? releaseType.name() : ReleaseType.UNKNOWN.name();
  }

  @Override
  public Class<String> fromType() {
    return String.class;
  }

  @Override
  public Class<ReleaseType> toType() {
    return ReleaseType.class;
  }
}
