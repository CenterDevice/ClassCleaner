package de.centerdevice.classcleaner.core.engine;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedSubgraph;

import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.CodeReference;

public class ReferenceGraph {
	DirectedGraph<CodeElement, CodeElemetEdge> directedGraph = new DefaultDirectedGraph<>(CodeElemetEdge.class);

	public ReferenceGraph(Collection<CodeReference> reference) {
		addReferences(reference);
	}

	private void addReferences(Collection<CodeReference> reference) {
		for (CodeReference codeReference : reference) {
			addReference(codeReference);
		}
	}

	private void addReference(CodeReference reference) {
		if (reference.getSource() != null && reference.getDestination() != null) {
			addReference(reference.getSource(), reference.getDestination());
		}
	}

	protected void addReference(CodeElement source, CodeElement destination) {
		directedGraph.addVertex(source);
		directedGraph.addVertex(destination);
		directedGraph.addEdge(source, destination);
	}

	public Set<CodeElement> getCycles() {
		CycleDetector<CodeElement, CodeElemetEdge> cycleDetector = new CycleDetector<>(directedGraph);

		return cycleDetector.findCycles();
	}

	public Set<CodeElement> getAllElements() {
		return directedGraph.vertexSet();
	}

	public Set<CodeElement> getReferringElements(CodeElement element) {
		return directedGraph.incomingEdgesOf(element).stream().map(edge -> directedGraph.getEdgeSource(edge))
				.collect(Collectors.toSet());
	}

	public Set<CodeElement> getReferencedElements(CodeElement element) {
		return directedGraph.outgoingEdgesOf(element).stream().map(edge -> directedGraph.getEdgeTarget(edge))
				.collect(Collectors.toSet());
	}

	public List<Set<CodeElement>> getReferenceGroups() {
		return getReferenceGroups(directedGraph);
	}

	public List<Set<CodeElement>> getReferenceGroups(ClassInfo classInfo) {
		return getReferenceGroups(getSubgraphOfClass(classInfo));
	}

	private List<Set<CodeElement>> getReferenceGroups(DirectedGraph<CodeElement, CodeElemetEdge> directedGraph) {
		return new ConnectivityInspector<>(directedGraph).connectedSets();
	}

	private DirectedSubgraph<CodeElement, CodeElemetEdge> getSubgraphOfClass(ClassInfo classInfo) {
		Set<CodeElemetEdge> edgeSubset = directedGraph.edgeSet().stream().filter(edge -> edge.isWithinClass(classInfo))
				.collect(Collectors.toSet());

		Set<CodeElement> vertexSubset = edgeSubset.stream()
				.flatMap(edge -> Stream.of(edge.getSourceElement(), edge.getTargetElement()))
				.collect(Collectors.toSet());

		return new DirectedSubgraph<>(directedGraph, vertexSubset, edgeSubset);
	}
}
