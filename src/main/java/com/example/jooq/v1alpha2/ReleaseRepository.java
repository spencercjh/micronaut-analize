package com.example.jooq.v1alpha2;

import ai.inceptio.chariot.v1alpha2.resources.Release;
import ai.inceptio.chariot.v1alpha2.resources.ReleaseDetail;
import ai.inceptio.chariot.v1alpha2.resources.ReleaseType;
import com.example.jooq.PagedList;
import com.example.jooq.PersistenceException;
import lombok.Builder;
import lombok.Getter;


/**
 * The interface Release repository.
 *
 * @author spencercjh
 */
public interface ReleaseRepository {
  /**
   * get release by title and ReleaseType with a combined index
   *
   * @param title       like bundle-v0.7.3-d20200529-1-1-g35608e1a5.yaml
   * @param releaseType {@link ReleaseType}
   * @return release
   */
  ReleaseDetail getByTitleAndType(String title, ReleaseType releaseType);

  /**
   * create release
   *
   * @param toCreate release to create
   * @return created release
   */
  ReleaseDetail create(ReleaseDetail toCreate);

  /**
   * update release
   *
   * @param toUpdate release to update
   * @return updated release
   */
  ReleaseDetail update(ReleaseDetail toUpdate) throws PersistenceException, PersistenceException;

  /**
   * Query paged list.
   *
   * @param page     the page
   * @param pageSize the page size
   * @param filter   the filter
   * @param sort     the sort
   * @return the paged list
   */
  PagedList<Release> query(int page, int pageSize, Filter filter, Sort sort);

  /**
   * The enum Sort.
   */
  enum Sort {
    /**
     * Create time desc sort.
     */
    CREATE_TIME_DESC,
    /**
     * Update time desc sort.
     */
    UPDATE_TIME_DESC
  }

  /**
   * The type Filter.
   */
  @Builder
  @Getter
  class Filter {
    /**
     * The Stage.
     */
    String stage;
    /**
     * The Title.
     */
    String titleFuzzy;
    /**
     * release type
     */
    ReleaseType type;
  }
}
