package com.example.jdbc.repositories;

import com.example.jdbc.entities.ReleaseDO;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

/**
 * @author spencercjh
 */
@JdbcRepository(dialect = Dialect.MYSQL)
public interface ReleaseRepository extends CrudRepository<ReleaseDO, Integer> {

}
