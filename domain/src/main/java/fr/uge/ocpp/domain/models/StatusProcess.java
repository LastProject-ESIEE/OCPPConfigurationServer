package fr.uge.ocpp.domain.models;

/** The status of the process launched on a charge point. */
public enum StatusProcess {

  /** The process is not launched. */
  PENDING,

  /** The process has been accepted and is in progress. */
  PROCESSING,

  /** The process has been done successfully. */
  FINISHED,

  /** The process has failed. */
  FAILED
}
