package com.example.jdbc.converters;

import io.micronaut.context.annotation.Factory;
import io.micronaut.core.convert.TypeConverter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author spencercjh
 */
@Factory
@Slf4j
public class MultiStringListConverter {
  @Singleton
  public TypeConverter<String, List<String>> stringListTypeConverter() {
    return (object, targetType, context) -> {
      log.trace("trigger MultiStringListConverter");
      return Optional.of(Arrays.asList(object.split(",")));
    };
  }

  @Singleton
  public TypeConverter<List<String>, String> listStringTypeConverter() {
    return (object, targetType, context) -> {
      log.trace("trigger MultiStringListConverter");
      return Optional.of(String.join(",", object));
    };
  }
}
