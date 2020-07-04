package com.example.jooq.v1alpha2.jooq.mappers;

import ai.inceptio.chariot.v1alpha2.resources.Release;
import ai.inceptio.chariot.v1alpha2.resources.ReleaseDetail;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.jooq.Record;
import org.jooq.RecordMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.jooq.v1alpha2.jooq.tables.ReleaseTable.*;


/**
 * @author Jiahao Cai
 */
@Slf4j
@Singleton
public class ReleaseDetailRecordMapper implements RecordMapper<Record, ReleaseDetail> {
  @Inject
  private ReleaseRecordMapper releaseRecordMapper;

  public Record map(DSLContext ctx, ReleaseDetail entry, boolean isUpdate) {
    final Record record = isUpdate ?
      ctx.newRecord(FIELD_STAGES, FIELD_RELEASE_NOTE, FIELD_CONTENT, FIELD_FILES, FIELD_UPDATE_TIME, FIELD_SYNC_UPDATE_TIME) :
      ctx.newRecord(FIELD_TITLE, FIELD_GROUP, FIELD_PROJECT, FIELD_STAGES, FIELD_RELEASE_TYPE, FIELD_RELEASE_NOTE,
        FIELD_CONTENT, FIELD_FILES, FIELD_CREATE_TIME, FIELD_UPDATE_TIME, FIELD_SYNC_CREATE_TIME, FIELD_SYNC_UPDATE_TIME);
    final Release release = entry.getRelease();
    if (!isUpdate) {
      if (!release.getCreateTime().equals(Timestamp.getDefaultInstance())) {
        record.with(FIELD_CREATE_TIME, new java.sql.Timestamp(release.getCreateTime().getSeconds() * 1000));
      }
      record
        .with(FIELD_TITLE, release.getTitle())
        .with(FIELD_GROUP, release.getGroup())
        .with(FIELD_PROJECT, release.getProject())
        .with(FIELD_RELEASE_TYPE, release.getReleaseType())
        .with(FIELD_SYNC_CREATE_TIME, new java.sql.Timestamp(release.getSyncCreateTime().getSeconds() * 1000));
    }
    if (!release.getUpdateTime().equals(Timestamp.getDefaultInstance())) {
      record.with(FIELD_UPDATE_TIME, new java.sql.Timestamp(release.getUpdateTime().getSeconds() * 1000));
    }
    return record
      .with(FIELD_CONTENT, entry.getContent())
      .with(FIELD_FILES, JSON.valueOf(release.getFilesList()
        .stream()
        .map(file -> {
          try {
            return JsonFormat.printer().omittingInsignificantWhitespace().print(file);
          } catch (InvalidProtocolBufferException e) {
            log.error("Serialize release detail failed", e);
            return "{}";
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.joining(",", "[", "]"))))
      .with(FIELD_RELEASE_NOTE, entry.getReleaseNote())
      .with(FIELD_STAGES, new HashSet<>(release.getStageList()).toArray(new String[]{}))
      .with(FIELD_SYNC_UPDATE_TIME, new java.sql.Timestamp(release.getSyncUpdateTime().getSeconds() * 1000));
  }

  @Override
  public ReleaseDetail map(Record record) {
    return ReleaseDetail.newBuilder()
      .setRelease(releaseRecordMapper.map(record))
      .setContent(record.get(FIELD_CONTENT))
      .setReleaseNote(record.get(FIELD_RELEASE_NOTE))
      .build();
  }
}
