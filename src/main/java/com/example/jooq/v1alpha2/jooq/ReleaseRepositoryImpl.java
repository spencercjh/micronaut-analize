package com.example.jooq.v1alpha2.jooq;

import ai.inceptio.chariot.v1alpha2.resources.Release;
import ai.inceptio.chariot.v1alpha2.resources.ReleaseDetail;
import ai.inceptio.chariot.v1alpha2.resources.ReleaseType;
import com.example.jooq.PagedList;
import com.example.jooq.v1alpha2.ReleaseRepository;
import com.example.jooq.v1alpha2.jooq.mappers.ReleaseDetailRecordMapper;
import com.example.jooq.v1alpha2.jooq.mappers.ReleaseRecordMapper;
import com.google.protobuf.util.Timestamps;
import io.micronaut.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.jooq.SortOrder;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

import static com.example.jooq.v1alpha2.jooq.tables.ReleaseTable.*;


/**
 * @author Jiahao Cai
 */
@Singleton
@Slf4j
public class ReleaseRepositoryImpl implements ReleaseRepository {
  @Inject
  private DSLContext dslContext;
  @Inject
  private ReleaseRecordMapper releaseRecordMapper;
  @Inject
  private ReleaseDetailRecordMapper releaseDetailRecordMapper;

  @Override
  public ReleaseDetail getByTitleAndType(String title, ReleaseType releaseType) {
    return dslContext.selectFrom(TABLE_NAME)
      .where(FIELD_TITLE.eq(title)
        .and(FIELD_RELEASE_TYPE.eq(releaseType)))
      .fetchOne(releaseDetailRecordMapper);
  }

  @Override
  public ReleaseDetail create(ReleaseDetail toCreate) {
    final ReleaseDetail created = toCreate.toBuilder().setRelease(toCreate.getRelease()
      .toBuilder()
      .setSyncCreateTime(Timestamps.fromMillis(System.currentTimeMillis()))
      .setSyncUpdateTime(Timestamps.fromMillis(System.currentTimeMillis())))
      .build();
    dslContext.insertInto(TABLE_NAME)
      .set(releaseDetailRecordMapper.map(dslContext, created, false))
      .execute();
    return created;
  }

  @Override
  public ReleaseDetail update(ReleaseDetail toUpdate) throws PersistenceException {
    final ReleaseDetail updated = toUpdate.toBuilder().setRelease(toUpdate.getRelease()
      .toBuilder()
      .setSyncUpdateTime(Timestamps.fromMillis(System.currentTimeMillis())))
      .build();
    final int row = dslContext.update(TABLE_NAME)
      .set(releaseDetailRecordMapper.map(dslContext, updated, true))
      .where(FIELD_ID.eq(updated.getRelease().getId()))
      .execute();
    if (row != 1) {
      throw new PersistenceException(String.format("Update existed release: %d failed", toUpdate.getRelease().getId()));
    }
    return updated;
  }

  @Override
  public PagedList<Release> query(int page, int pageSize, Filter filter, Sort sort) {
    final List<Condition> conditions = new ArrayList<>();
    if (StringUtils.isNotEmpty(filter.getTitleFuzzy())) {
      conditions.add(FIELD_TITLE.like("%" + filter.getTitleFuzzy().trim() + "%"));
    }
    if (StringUtils.isNotEmpty(filter.getStage())) {
      conditions.add(FIELD_STAGES.like("%" + filter.getStage().toUpperCase().trim() + "%"));
    }
    if (filter.getType() != null && !filter.getType().equals(ReleaseType.UNKNOWN)) {
      conditions.add(FIELD_RELEASE_TYPE.eq(filter.getType()));
    }
    final int total = dslContext.fetchCount(TABLE_NAME, conditions);
    final int totalPage = pageSize <= 0 ? 1 : (total - 1) / pageSize + 1;
    if (page > totalPage) {
      page = totalPage;
    }
    SortField<?>[] sorts;
    switch (sort) {
      case CREATE_TIME_DESC:
        sorts = new SortField[]{FIELD_CREATE_TIME.sort(SortOrder.DESC)};
        break;
      case UPDATE_TIME_DESC:
        sorts = new SortField[]{FIELD_UPDATE_TIME.sort(SortOrder.DESC)};
        break;
      default:
        sorts = new SortField[]{};
        break;
    }
    final List<Release> releases;
    if (page < 1 || pageSize < 1) {
      releases = dslContext
        .selectFrom(TABLE_NAME)
        .where(conditions)
        .orderBy(sorts)
        .fetch(releaseRecordMapper);
      page = 1;
      pageSize = releases.size();
    } else {
      releases = dslContext
        .selectFrom(TABLE_NAME)
        .where(conditions)
        .orderBy(sorts)
        .limit((page - 1) * pageSize, pageSize)
        .fetch(releaseRecordMapper);
    }
    return PagedList.of(page, pageSize, total, releases);
  }
}
