package de.centerdevice.classcleaner.core.recon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.centerdevice.classcleaner.core.engine.ReferenceClustering;
import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.ReferenceScope;

public class ReferenceReport {
	private final ReferenceScope scope;
	private final Map<ClassInfo, List<CodeReference>> references;
	private final Map<ClassInfo, ReferenceClustering> clusterings;

	public ReferenceReport(ReferenceScope scope, Map<ClassInfo, List<CodeReference>> references) {
		this.scope = scope;
		this.references = references;
		this.clusterings = new HashMap<>();
	}

	public ReferenceScope getScope() {
		return scope;
	}

	public List<CodeReference> getReferences(ClassInfo classInfo) {
		return references.get(classInfo);
	}

	public Set<ClassInfo> getClasses() {
		return references.keySet();
	}

	public ReferenceClustering getClustering(ClassInfo classInfo) {
		if (clusterings.containsKey(classInfo)) {
			return clusterings.get(classInfo);
		}

		ReferenceClustering createClustering = createClustering(classInfo);
		clusterings.put(classInfo, createClustering);
		return createClustering;
	}

	private ReferenceClustering createClustering(ClassInfo classInfo) {
		return new ReferenceClustering(getReferences(classInfo));
	}
}
