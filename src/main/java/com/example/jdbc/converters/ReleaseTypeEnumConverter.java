package com.example.jdbc.converters;

import ai.inceptio.chariot.v1alpha2.resources.ReleaseType;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.convert.TypeConverter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.Optional;

/**
 * @author spencercjh
 */
@Factory
@Slf4j
public class ReleaseTypeEnumConverter {
  @Singleton
  public TypeConverter<String, ReleaseType> stringToReleaseType() {
    return (object, targetType, context) -> {
      log.trace("trigger ReleaseTypeEnumConverter");
      return Optional.of(ReleaseType.valueOf(object)).or(() -> Optional.ofNullable(ReleaseType.UNKNOWN));
    };
  }

  @Singleton
  public TypeConverter<ReleaseType, String> releaseTypeToString() {
    return (object, targetType, context) -> {
      log.trace("trigger ReleaseTypeEnumConverter");
      return Optional.of(object.name());
    };
  }
}
