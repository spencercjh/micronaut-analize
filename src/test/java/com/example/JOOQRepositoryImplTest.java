package com.example;

import ai.inceptio.chariot.v1alpha2.resources.Release;
import ai.inceptio.chariot.v1alpha2.resources.ReleaseDetail;
import ai.inceptio.chariot.v1alpha2.resources.ReleaseFile;
import ai.inceptio.chariot.v1alpha2.resources.ReleaseType;
import com.example.jooq.PagedList;
import com.example.jooq.v1alpha2.ReleaseRepository;
import com.google.protobuf.util.Timestamps;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class JOOQRepositoryImplTest {
  @Inject
  private ReleaseRepository releaseRepository;

  @Test
  void create() {
    releaseRepository.create(ReleaseDetail.newBuilder()
      .setRelease(Release.newBuilder()
        .setTitle("title")
        .setGroup("group")
        .setProject("project")
        .addStage("A").addStage("B")
        .setReleaseType(ReleaseType.MAP)
        .addFiles(ReleaseFile.newBuilder()
          .setName("name")
          .build())
        .setCreateTime(Timestamps.fromMillis(System.currentTimeMillis()))
        .setUpdateTime(Timestamps.fromMillis(System.currentTimeMillis()))
        .setSyncCreateTime(Timestamps.fromMillis(System.currentTimeMillis()))
        .setSyncUpdateTime(Timestamps.fromMillis(System.currentTimeMillis()))
        .build())
      .setContent("Content")
      .setReleaseNote("Note")
      .build());
  }
}