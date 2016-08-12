package de.centerdevice.classcleaner.core.recon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.centerdevice.classcleaner.core.engine.ReferenceGraph;
import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.ReferenceScope;

public class ReferenceReport {
	private final ReferenceScope scope;
	private final Map<ClassInfo, List<CodeReference>> references;
	private final Map<ClassInfo, ReferenceGraph> graphs;

	public ReferenceReport(ReferenceScope scope, Map<ClassInfo, List<CodeReference>> references) {
		this.scope = scope;
		this.references = references;
		this.graphs = new HashMap<>();
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

	public ReferenceGraph getReferenceGraph(ClassInfo classInfo) {
		if (graphs.containsKey(classInfo)) {
			return graphs.get(classInfo);
		}

		ReferenceGraph graph = createGraph(classInfo);
		graphs.put(classInfo, graph);
		return graph;
	}

	private ReferenceGraph createGraph(ClassInfo classInfo) {
		return new ReferenceGraph(getReferences(classInfo));
	}
}
