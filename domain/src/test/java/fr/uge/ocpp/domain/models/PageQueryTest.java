package fr.uge.ocpp.domain.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class PageQueryTest {

  @Test
  void should_reject_negative_page() {
    assertThatThrownBy(() -> new PageQuery(-1, 10, Sort.DESC))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("page >= 0 and size >= 1 required");
  }

  @Test
  void should_reject_non_positive_size() {
    assertThatThrownBy(() -> new PageQuery(0, 0, Sort.DESC))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void should_default_sort_to_desc() {
    assertThat(new PageQuery(0, 10, null).sort()).isEqualTo(Sort.DESC);
  }
}
