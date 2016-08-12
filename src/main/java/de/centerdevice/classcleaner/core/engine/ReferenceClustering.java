package de.centerdevice.classcleaner.core.engine;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.CodeReference;

public class ReferenceClustering {
	DirectedGraph<CodeElement, DefaultEdge> directedGraph = new DefaultDirectedGraph<CodeElement, DefaultEdge>(
			DefaultEdge.class);

	public ReferenceClustering(Collection<CodeReference> reference) {
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
		if (source.getClassName().equals(destination.getClassName())) {
			directedGraph.addVertex(source);
			directedGraph.addVertex(destination);
			directedGraph.addEdge(source, destination);
		}
	}

	public List<Set<CodeElement>> getReferenceGroups() {
		ConnectivityInspector<CodeElement, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(
				directedGraph);
		return connectivityInspector.connectedSets();
	}
}
