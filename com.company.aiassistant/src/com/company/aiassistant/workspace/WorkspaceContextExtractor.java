package com.company.aiassistant.workspace;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.IViewPart;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;

/**
 * Reads Package Explorer selection.
 */
public final class WorkspaceContextExtractor {

    private WorkspaceContextExtractor() {
    }

    public static WorkspaceContext extract() {

        WorkspaceContext context =
                new WorkspaceContext();

        try {

            /*ISelection selection =
                    PlatformUI.getWorkbench()
                              .getActiveWorkbenchWindow()
                              .getSelectionService()
                              .getSelection();*/
        	
        	ISelection selection = null;

        	try {

        	    IViewPart packageExplorer =
        	            PlatformUI.getWorkbench()
        	                      .getActiveWorkbenchWindow()
        	                      .getActivePage()
        	                      .findView(
        	                              "org.eclipse.jdt.ui.PackageExplorer");

        	    if (packageExplorer instanceof PackageExplorerPart) {

        	        selection =
        	                ((PackageExplorerPart)
        	                        packageExplorer)
        	                        .getSite()
        	                        .getSelectionProvider()
        	                        .getSelection();
        	    }

        	} catch (Exception e) {

        	    e.printStackTrace();
        	}

            if (!(selection instanceof IStructuredSelection)) {

//                return context;
            	populateFromEditor(
            	        context);

            	return context;
            }

            Object selected =
                    ((IStructuredSelection) selection)
                            .getFirstElement();

            if (!(selected instanceof IJavaElement)) {

//                return context;
            	populateFromEditor(
            	        context);

            	return context;
            }

            IJavaElement javaElement =
                    (IJavaElement) selected;

            IProject project =
                    javaElement.getJavaProject()
                               .getProject();

            context.setSelectedProject(
                    project.getName());

            if (javaElement instanceof IPackageFragment) {

                IPackageFragment pkg =
                        (IPackageFragment) javaElement;

                context.setSelectedPackage(
                        pkg.getElementName());
            }

            if (javaElement instanceof ICompilationUnit) {

                ICompilationUnit unit =
                        (ICompilationUnit) javaElement;

                context.setSelectedFile(
                        unit.getElementName());

                context.setSelectedPackage(
                        unit.getParent()
                            .getElementName());

                context.setSelectedClass(
                        unit.getElementName()
                            .replace(".java", ""));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return context;
    }
    
    private static void populateFromEditor(
            WorkspaceContext context) {

        try {

            ICompilationUnit unit =
                    WorkspaceSelectionUtil
                            .getActiveCompilationUnit();

            if (unit == null) {

                return;
            }

            context.setSelectedProject(
                    unit.getJavaProject()
                        .getProject()
                        .getName());

            context.setSelectedFile(
                    unit.getElementName());

            context.setSelectedClass(
                    unit.getElementName()
                        .replace(
                                ".java",
                                ""));

            if (unit.getParent()
                    instanceof IPackageFragment) {

                context.setSelectedPackage(
                        ((IPackageFragment)
                                unit.getParent())
                                        .getElementName());
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}