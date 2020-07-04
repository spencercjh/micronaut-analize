package com.example.jooq.v1alpha2.jooq.mappers;

import ai.inceptio.chariot.v1alpha2.resources.Release;
import ai.inceptio.chariot.v1alpha2.resources.ReleaseFile;
import ai.inceptio.chariot.v1alpha2.resources.ReleaseType;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.Timestamps;
import io.micronaut.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jooq.JSON;
import org.jooq.Record;
import org.jooq.RecordMapper;

import javax.annotation.Nonnull;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Collections;

import static com.example.jooq.v1alpha2.jooq.tables.ReleaseTable.*;

/**
 * @author Jiahao Cai
 */
@Slf4j
@Singleton
public class ReleaseRecordMapper implements RecordMapper<Record, Release> {

  @Override
  public Release map(@Nonnull Record record) {
    try {
      final ReleaseType releaseType = record.get(FIELD_RELEASE_TYPE.getName(), FIELD_RELEASE_TYPE.getConverter());
      return Release.newBuilder()
        .setId(record.get(FIELD_ID))
        .setName(String.format("releases/%s-%s", releaseType.name().toLowerCase(), record.get(FIELD_TITLE)))
        .setTitle(record.get(FIELD_TITLE))
        .addAllStage(Arrays.asList(record.get(FIELD_STAGES.getName(), FIELD_STAGES.getConverter())))
        .setGroup(record.get(FIELD_GROUP))
        .setProject(record.get(FIELD_PROJECT))
        .setReleaseType(releaseType)
        .setReleaseNotePreview(cutReleaseNote(record.get(FIELD_RELEASE_NOTE)))
        .addAllFiles(deserializeFiles(record.get(FIELD_FILES)))
        .setCreateTime(Timestamps.fromMillis(record.get(FIELD_CREATE_TIME).getTime()))
        .setUpdateTime(Timestamps.fromMillis(record.get(FIELD_UPDATE_TIME).getTime()))
        .setSyncCreateTime(Timestamps.fromMillis(record.get(FIELD_SYNC_CREATE_TIME).getTime()))
        .setSyncUpdateTime(Timestamps.fromMillis(record.get(FIELD_SYNC_UPDATE_TIME).getTime()))
        .build();
    } catch (InvalidProtocolBufferException e) {
      log.error("Deserialize release files proto failed", e);
      throw new RuntimeException(e);
    }
  }

  private Iterable<? extends ReleaseFile> deserializeFiles(JSON json) throws InvalidProtocolBufferException {
    if (json == null) {
      return Collections.emptyList();
    }
    final Release.Builder builder = Release.newBuilder();
    final String actualJson = "{\"files\":" + json + "}";
    JsonFormat.parser().merge(actualJson, builder);
    return builder.build().getFilesList();
  }

  private String cutReleaseNote(String releaseNote) {
    return StringUtils.isEmpty(releaseNote) ? "" :
      releaseNote.length() < 100 ? releaseNote : releaseNote.substring(0, 100) + " ......";
  }
}
