package fr.uge.chargepointconfiguration.entities.dto;

/**
 * Configuration DTO which contains general information name and description.
 *
 * @param name configuration name
 * @param description configuration description
 */
public record ConfigurationGeneralDto(String name, String description) {

}
