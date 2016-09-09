package de.centerdevice.classcleaner.menu.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.centerdevice.classcleaner.ClassCleaner;
import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.FieldElement;
import de.centerdevice.classcleaner.core.model.MethodElement;
import de.centerdevice.classcleaner.core.model.ReferenceScope;
import de.centerdevice.classcleaner.core.recon.ReferenceReport;
import de.centerdevice.classcleaner.java.JavaSelectionConverter;

public class CreateDotFileHandler extends AbstractHandler implements IHandler {

	private static final String SHAPE_PLAIN = "plaintext";

	private static final String SHAPE_OVAL = "oval";

	private static final String SHAPE_BOX = "box";

	private static final String DIGRAPH = "digraph ";

	private static final String NODE_PREFIX = "node_";

	private static final String DOT = ".dot";

	private final JavaSelectionConverter selectionConverter = new JavaSelectionConverter();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			for (IFile file : selectionConverter.toFiles((IStructuredSelection) selection)) {
				analyzeFile(file);
			}
		}
		return null;
	}

	protected void analyzeFile(IFile file) {
		Job job = new Job("DotFile") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				ReferenceReport report = ClassCleaner.getInstance().generateReport(file, ReferenceScope.Project,
						monitor);
				createDotFile(file, report);
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	private void createDotFile(IFile file, ReferenceReport report) {
		Set<CodeReference> allReferences = getAllCodeReferences(report);
		Set<CodeElement> allElements = getAllCodeElements(allReferences);
		Map<CodeElement, String> nodes = createNodeDescriptions(allElements);

		String name = getFilename(file);
		File outFile = getOutFilePath(file, name);
		try (PrintWriter out = new PrintWriter(outFile)) {
			writeHeader(name, out);
			out.write("{\n");
			writeVertices(nodes, out);
			writeEdges(allReferences, nodes, out);
			out.write("}\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public File getOutFilePath(IFile file, String name) {
		return Paths.get(getTargetPath(file), name + DOT).toFile();
	}

	public String getTargetPath(IFile file) {
		return file.getProject().getLocation().toString();
	}

	public Set<CodeElement> getAllCodeElements(Set<CodeReference> allReferences) {
		return allReferences.stream().flatMap(reference -> Stream.of(reference.getSource(), reference.getDestination()))
				.collect(Collectors.toSet());
	}

	public Set<CodeReference> getAllCodeReferences(ReferenceReport report) {
		return report.getClasses().stream().flatMap(classInfo -> report.getReferences(classInfo).stream())
				.filter(reference -> reference.getSource() != null).collect(Collectors.toSet());
	}

	public Map<CodeElement, String> createNodeDescriptions(Set<CodeElement> allElements) {
		int i = 0;
		Map<CodeElement, String> nodeMap = new HashMap<>(allElements.size());
		for (CodeElement codeElement : allElements) {
			nodeMap.put(codeElement, NODE_PREFIX + (i++));
		}
		return nodeMap;
	}

	public String getFilename(IFile file) {
		return file.getName().replace("." + file.getFileExtension(), "");
	}

	public void writeHeader(String name, PrintWriter out) {
		out.write(DIGRAPH + name + "\n");
	}

	public void writeEdges(Set<CodeReference> allReferences, Map<CodeElement, String> nodeMap, PrintWriter out) {
		for (CodeReference codeReference : allReferences) {
			out.write("  " + nodeMap.get(codeReference.getSource()) + " -> "
					+ nodeMap.get(codeReference.getDestination()) + "\n");
		}
	}

	public void writeVertices(Map<CodeElement, String> nodeMap, PrintWriter out) {
		for (Entry<CodeElement, String> codeElement : nodeMap.entrySet()) {
			out.write("  " + codeElement.getValue() + " [label=\"" + codeElement.getKey().toString() + "\", shape=\""
					+ getShape(codeElement.getKey()) + "\"]\n");
		}
	}

	private String getShape(CodeElement element) {
		if (element instanceof MethodElement) {
			return SHAPE_BOX;
		} else if (element instanceof FieldElement) {
			return SHAPE_OVAL;
		}
		return SHAPE_PLAIN;
	}

}