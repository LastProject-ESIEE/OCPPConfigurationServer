package fr.uge.ocpp.domain.models;

import java.util.List;

/** A page of results. */
public record PageResult<T>(List<T> content, int page, int size, long totalElements) {}
