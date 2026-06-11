package com.copilot.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.*;

public class ProjectScanner {

    /**
     * Instantly builds a lightweight map of all packages, classes, and method signatures 
     * in the active project without reading the heavy method bodies.
     */
    public static String buildProjectSkeleton(IProject project) {
        if (project == null || !project.isOpen()) return "";

        StringBuilder skeleton = new StringBuilder();
        try {
            if (project.hasNature(JavaCore.NATURE_ID)) {
                IJavaProject javaProject = JavaCore.create(project);
                IPackageFragment[] packages = javaProject.getPackageFragments();
                
                for (IPackageFragment mypackage : packages) {
                    // Only scan actual source code folders (ignore compiled binaries and jars)
                    if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
                        for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
                            skeleton.append("File Path: ").append(unit.getResource().getProjectRelativePath().toString()).append("\n");
                            
                            for (IType type : unit.getTypes()) {
                                skeleton.append("  Class/Interface: ").append(type.getElementName()).append("\n");
                                
                                for (IField field : type.getFields()) {
                                    // Extract variable names
                                    skeleton.append("    Field: ").append(field.getElementName()).append("\n");
                                }
                                
                                for (IMethod method : type.getMethods()) {
                                    // Extract method signatures (e.g., saveCustomer)
                                    skeleton.append("    Method: ").append(method.getElementName()).append("()\n");
                                }
                            }
                            skeleton.append("\n");
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "Unable to scan project structure.";
        }
        return skeleton.toString();
    }
}