package fr.uge.chargepointconfiguration.configuration;

/**
 * DTO to display ConfigurationTranscription in front page.
 *
 * @param id Database id of the configuration stored.
 * @param fullname Adaquate description of the technical key used for chargepoint configuration.
 */
public record ConfigurationTranscriptorDto(
    int id,
    String fullname
) {
}
