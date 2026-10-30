package fr.uge.ocpp.domain.models;

/** A pagination query. */
public record PageQuery(int page, int size, Sort sort) {}
