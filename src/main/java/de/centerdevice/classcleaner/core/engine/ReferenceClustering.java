package de.centerdevice.classcleaner.core.engine;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.CodeReference;

public class ReferenceClustering {
	DirectedGraph<CodeElement, DefaultEdge> directedGraph = new DefaultDirectedGraph<CodeElement, DefaultEdge>(
			DefaultEdge.class);

	public void addReference(CodeReference reference) {

	}
}
