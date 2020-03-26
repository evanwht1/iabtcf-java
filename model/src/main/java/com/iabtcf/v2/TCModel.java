package com.iabtcf.v2;

import java.util.Optional;

/**
 * @author evanwht1
 */
public interface TCModel {

    CoreString getCoreString();

    Optional<OutOfBandConsent> getOutOfBandConsent();

    Optional<PublisherTC> getPublisherTC();
}
