package com.iabtcf.v2.encoder;

import com.iabtcf.v2.CoreString;
import com.iabtcf.v2.OutOfBandConsent;
import com.iabtcf.v2.PublisherTC;
import com.iabtcf.v2.TCModel;

/**
 * @author evanwht1@gmail.com
 */
public class TCModelBuilder {

	private CoreString.Builder coreStringBuilder;
	private OutOfBandConsent.Builder outOfBandBuilder;
	private PublisherTC.Builder publisherTCBuilder;

	public TCModelBuilder() {
		coreStringBuilder = CoreString.newBuilder();
		outOfBandBuilder = OutOfBandConsent.newBuilder();
	}

	public TCModelBuilder(TCModel model) {
		coreStringBuilder = CoreString.newBuilder(model.getCoreString());
		if (model.getOutOfBandConsent().isPresent()) {
			outOfBandBuilder = OutOfBandConsent.newBuilder(model.getOutOfBandConsent().get());
		}
		if (model.getPublisherTC().isPresent()) {
			publisherTCBuilder = PublisherTC.newBuilder(model.getPublisherTC().get());
		}
	}

	public CoreString.Builder getCoreStringBuilder() {
		return coreStringBuilder;
	}

	public OutOfBandConsent.Builder getOutOfBandBuilder() {
		if (outOfBandBuilder == null) {
			outOfBandBuilder = OutOfBandConsent.newBuilder();
		}
		return outOfBandBuilder;
	}

	boolean hasOutOfBandConsentField() {
		return outOfBandBuilder != null;
	}

	public PublisherTC.Builder getPublisherTCBuilder() {
		if (publisherTCBuilder == null) {
			publisherTCBuilder = PublisherTC.newBuilder();
		}
		return publisherTCBuilder;
	}

	boolean hasPublisherTCFields() {
		return publisherTCBuilder != null;
	}

	public String buildTCString() {
		return TCModelEncoder.encode(this);
	}
}
