package com.example.jooq;

import lombok.Data;
import lombok.Value;

import java.util.List;

/**
 * @author bestmike007
 */
@Data
@Value(staticConstructor = "of")
public class PagedList<T> {
  private int page;
  private int pageSize;
  private int total;
  private List<T> list;
}
