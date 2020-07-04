package com.example;

import ai.inceptio.chariot.v1alpha2.resources.*;
import com.example.jooq.PagedList;
import com.example.jooq.v1alpha2.ReleaseRepository;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
@Singleton
class JOOQReleaseServiceTest {
  private final Release mockRelease = Release.newBuilder()
    .setProject("project")
    .setGroup("group")
    .addStage("DVT")
    .setTitle("title")
    .setReleaseNotePreview("note")
    .setReleaseType(ReleaseType.CYBERTRON)
    .addFiles(ReleaseFile.newBuilder()
      .setChecksums(Checksums.newBuilder()
        .setMd5("md5")
        .setSha1("sha1")
        .setSha256("sha256")
        .build())
      .setName("name")
      .setSize(123)
      .build())
    .build();
  private final ReleaseDetail mock = ReleaseDetail.newBuilder()
    .setRelease(mockRelease)
    .setReleaseNote("note")
    .setContent("bundle")
    .build();
  @Inject
  private ReleaseRepository releaseDetailRepository;
  @Inject
  private DataSource dataSource;

  @BeforeEach
  void prepareData() {
    for (int i = 0; i < 20; i++) {
      releaseDetailRepository.create(mock.toBuilder()
        .setRelease(mock.getRelease().toBuilder()
          .setTitle(mockRelease.getTitle() + i)
          .build())
        .build());
    }
    for (int i = 20; i < 50; i++) {
      releaseDetailRepository.create(mock.toBuilder()
        .setRelease(mock.getRelease().toBuilder()
          .setTitle(mockRelease.getTitle() + i)
          .addStage("LST")
          .build())
        .build());
    }
  }

  @AfterEach
  void tearDown() throws SQLException {
    try (final Connection connection = dataSource.getConnection()) {
      connection.prepareStatement("truncate data_segments;").execute();
      connection.prepareStatement("truncate deleted;").execute();
      connection.prepareStatement("truncate missions;").execute();
      connection.prepareStatement("truncate moments;").execute();
      connection.prepareStatement("truncate record_copies;").execute();
      connection.prepareStatement("truncate records;").execute();
      connection.prepareStatement("truncate vehicles;").execute();
      connection.prepareStatement("truncate releases;").execute();
    }
  }

  @Test
  void listReleases() {
    final int page = 1;
    final int pageSize = 50;
    final String stage = "LST";
    PagedList<Release> result = releaseDetailRepository.query(page, pageSize, ReleaseRepository.Filter.builder()
      .stage(stage).build(), ReleaseRepository.Sort.CREATE_TIME_DESC);
    assertNotNull(result);
    assertEquals(page, result.getPage());
    assertEquals(pageSize, result.getPageSize());
    assertEquals(30, result.getList().size());
    assertEquals(30, result.getTotal());
  }
}