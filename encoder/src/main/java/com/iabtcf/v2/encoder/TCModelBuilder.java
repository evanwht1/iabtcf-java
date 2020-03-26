package com.iabtcf.v2.encoder;

import com.iabtcf.v2.TCModel;

/**
 * @author evanwht1@gmail.com
 */
public class TCModelBuilder {

	CoreStringBuilder coreStringBuilder;
	OutOfBandBuilder outOfBandBuilder;
	PublisherTCBuilder publisherTCBuilder;

	public TCModelBuilder() {
		coreStringBuilder = new CoreStringBuilder();
		outOfBandBuilder = new OutOfBandBuilder();
		publisherTCBuilder = new PublisherTCBuilder();
	}

	public TCModelBuilder(TCModel model) {
		coreStringBuilder = new CoreStringBuilder(model.getCoreString());
		if (model.getOutOfBandConsent().isPresent()) {
			outOfBandBuilder = new OutOfBandBuilder(model.getOutOfBandConsent().get());
		} else {
			outOfBandBuilder = new OutOfBandBuilder();
		}
		if (model.getPublisherTC().isPresent()) {
			publisherTCBuilder = new PublisherTCBuilder(model.getPublisherTC().get());
		} else {
			publisherTCBuilder = new PublisherTCBuilder();
		}
	}

	public CoreStringBuilder getCoreStringBuilder() {
		return coreStringBuilder;
	}

	public OutOfBandBuilder getOutOfBandBuilder() {
		return outOfBandBuilder;
	}

	public PublisherTCBuilder getPublisherTCBuilder() {
		return publisherTCBuilder;
	}

	public String build() {
		return TCModelEncoder.encode(this);
	}
}
