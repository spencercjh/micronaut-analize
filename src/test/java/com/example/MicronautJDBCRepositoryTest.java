package com.example;

import ai.inceptio.chariot.v1alpha2.resources.ReleaseType;
import com.example.jdbc.entities.ReleaseDO;
import com.example.jdbc.repositories.ReleaseRepository;
import io.micronaut.test.annotation.MicronautTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
@Slf4j
class MicronautJDBCRepositoryTest {
  @Inject
  private ReleaseRepository releaseRepository;

  private static Stream<ReleaseDO> provideReleaseDO() {
    final ReleaseDO withoutSyncTime = ReleaseDO.builder()
      .title("v1.2.3")
      .group("infra")
      .project("chariot")
      .stages(Arrays.asList("DVT,LST".split(",")))
      .releaseType(ReleaseType.BUNDLE)
      .releaseNote("releaseNote")
      .content("content")
      .files("[]")
      .createTime(new Timestamp(System.currentTimeMillis() - 1000000))
      .updateTime(new Timestamp(System.currentTimeMillis() - 1000000))
      .build();
    return Stream.of(withoutSyncTime
        .setStages(Collections.singletonList("DVT"))
        .setSyncCreateTime(new Timestamp(System.currentTimeMillis()))
        .setSyncUpdateTime(new Timestamp(System.currentTimeMillis())),
      withoutSyncTime.setStages(Collections.singletonList("")),
      withoutSyncTime
    );
  }

  @ParameterizedTest
  @MethodSource("provideReleaseDO")
  void testCreate(ReleaseDO toSave) {
    final ReleaseDO saved = releaseRepository.save(toSave);
    assertNotNull(saved.getId());
    assertEquals(toSave.getStages(), saved.getStages());
    assertEquals(toSave.getReleaseType(), saved.getReleaseType());
  }

  @ParameterizedTest
  @MethodSource("provideReleaseDO")
  void testRead(ReleaseDO toSaveFirst) {
    final ReleaseDO saved = releaseRepository.save(toSaveFirst);
    assertEquals(saved.getContent(), releaseRepository.findById(saved.getId()).get().getContent());
  }
}