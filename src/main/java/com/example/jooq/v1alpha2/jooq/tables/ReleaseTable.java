package com.example.jooq.v1alpha2.jooq.tables;

import ai.inceptio.chariot.v1alpha2.resources.ReleaseType;
import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.Record;
import org.jooq.Table;

import java.sql.Timestamp;

import static com.example.jooq.converter.MultivaluedStringToArrayConverter.STRING_ARRAY_TYPE;
import static com.example.jooq.converter.ReleaseTypeConverter.RELEASE_TYPE_DATA_TYPE;
import static org.jooq.impl.DSL.*;

/**
 * @author Jiahao Cai
 */
public final class ReleaseTable {
  public static final Table<Record> TABLE_NAME = table("releases");

  public static final Field<Integer> FIELD_ID =
    field(name("id"), Integer.class);
  public static final Field<String> FIELD_TITLE =
    field(name("title"), String.class);
  public static final Field<String> FIELD_GROUP =
    field(name("group"), String.class);
  public static final Field<String> FIELD_PROJECT =
    field(name("project"), String.class);
  public static final Field<String[]> FIELD_STAGES =
    field(name("stages"), STRING_ARRAY_TYPE);
  public static final Field<ReleaseType> FIELD_RELEASE_TYPE =
    field(name("release_type"), RELEASE_TYPE_DATA_TYPE);
  public static final Field<String> FIELD_RELEASE_NOTE =
    field(name("release_note"), String.class);
  public static final Field<String> FIELD_CONTENT =
    field(name("content"), String.class);
  public static final Field<JSON> FIELD_FILES =
    field(name("files"), JSON.class);
  public final static Field<Timestamp> FIELD_CREATE_TIME =
    field(name("create_time"), Timestamp.class);
  public final static Field<Timestamp> FIELD_UPDATE_TIME =
    field(name("update_time"), Timestamp.class);
  public final static Field<Timestamp> FIELD_SYNC_CREATE_TIME =
    field(name("sync_create_time"), Timestamp.class);
  public final static Field<Timestamp> FIELD_SYNC_UPDATE_TIME =
    field(name("sync_update_time"), Timestamp.class);
}
