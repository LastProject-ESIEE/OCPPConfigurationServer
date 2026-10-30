package fr.uge.ocpp.domain.models;

/** The step of the process a charge point is in. */
public enum Step {

  /** The charge point will update its firmware. */
  FIRMWARE,

  /** The charge point will change values in its configuration. */
  CONFIGURATION
}
