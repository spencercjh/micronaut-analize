package com.example.jdbc.entities;

import ai.inceptio.chariot.v1alpha2.resources.ReleaseType;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.TypeDef;
import io.micronaut.data.model.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author spencercjh
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Data
@MappedEntity(value = "releases")
public class ReleaseDO {
  @Id
  @GeneratedValue(GeneratedValue.Type.IDENTITY)
  private Integer id;

  /**
   * actual display name like v0.7.4-d20200608-1-9-g87eca2d39
   */
  private String title;

  private String group;

  private String project;

  /**
   * A,B,C
   */
  @TypeDef(type = DataType.STRING)
  private List<String> stages;

  /**
   * a enum defined in inceptioapi
   */
  @TypeDef(type = DataType.STRING)
  private ReleaseType releaseType;

  private String releaseNote;

  private String content;

  private String files;

  private Timestamp createTime;

  private Timestamp updateTime;

  private Timestamp syncCreateTime;

  private Timestamp syncUpdateTime;
}