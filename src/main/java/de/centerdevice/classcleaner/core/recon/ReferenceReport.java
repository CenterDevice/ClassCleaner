package de.centerdevice.classcleaner.core.recon;

import java.util.List;

import de.centerdevice.classcleaner.core.engine.ReferenceClustering;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.ReferenceScope;

public class ReferenceReport {
	private final ReferenceScope scope;
	private final List<CodeReference> references;

	private ReferenceClustering lazyClustering = null;

	public ReferenceReport(ReferenceScope scope, List<CodeReference> references) {
		this.scope = scope;
		this.references = references;
	}

	public ReferenceScope getScope() {
		return scope;
	}

	public List<CodeReference> getReferences() {
		return references;
	}

	public ReferenceClustering getClustering() {
		if (lazyClustering == null) {
			lazyClustering = createClustering();
		}

		return lazyClustering;
	}

	private ReferenceClustering createClustering() {
		return new ReferenceClustering(getReferences());
	}

}
