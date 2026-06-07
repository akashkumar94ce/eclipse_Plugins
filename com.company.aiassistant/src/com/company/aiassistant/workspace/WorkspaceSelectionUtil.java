package com.company.aiassistant.workspace;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageDeclaration;

public final class WorkspaceSelectionUtil {

	private WorkspaceSelectionUtil() {
	}

	public static IStructuredSelection getSelection() {

		try {

			ISelection selection = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getSelectionService()
					.getSelection();

			if (selection instanceof IStructuredSelection) {

				return (IStructuredSelection) selection;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	public static IProject getSelectedProject() {

		try {

			IStructuredSelection selection = getSelection();

			if (selection == null) {

				return null;
			}

			Object element = selection.getFirstElement();

			if (element instanceof IProject) {

				return (IProject) element;
			}

			if (element instanceof IJavaProject) {

				return ((IJavaProject) element).getProject();
			}

			if (element instanceof IJavaElement) {

				return ((IJavaElement) element).getJavaProject().getProject();
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	public static IJavaProject getSelectedJavaProject() {

		try {

			IStructuredSelection selection = getSelection();

			if (selection == null) {

				return null;
			}

			Object element = selection.getFirstElement();

			if (element instanceof IJavaProject) {

				return (IJavaProject) element;
			}

			if (element instanceof IJavaElement) {

				return ((IJavaElement) element).getJavaProject();
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	public static IPackageFragment getSelectedPackage() {

		try {

			IStructuredSelection selection = getSelection();

			if (selection == null) {

				return null;
			}

			Object element = selection.getFirstElement();

			if (element instanceof IPackageFragment) {

				return (IPackageFragment) element;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	public static IResource getSelectedResource() {

		try {

			IStructuredSelection selection = getSelection();

			if (selection == null) {

				return null;
			}

			Object element = selection.getFirstElement();

			if (element instanceof IResource) {

				return (IResource) element;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	public static IPackageFragmentRoot getSelectedSourceFolder() {

		try {

			IStructuredSelection selection = getSelection();

			if (selection == null) {

				return null;
			}

			Object element = selection.getFirstElement();

			if (element instanceof IPackageFragmentRoot) {

				return (IPackageFragmentRoot) element;
			}

			if (element instanceof IPackageFragment) {

				return (IPackageFragmentRoot) ((IPackageFragment) element)
						.getParent();
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	public static IJavaProject getActiveEditorProject() {

		try {

			IEditorPart editor = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();

			if (editor == null) {

				return null;
			}

			IEditorInput input = editor.getEditorInput();

			IFile file = (IFile) input.getAdapter(IFile.class);

			if (file == null) {

				return null;
			}

			IProject project = file.getProject();

			return JavaCore.create(project);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	public static IPackageFragmentRoot findSourceRoot(IJavaProject project)
			throws Exception {

		for (IPackageFragmentRoot root : project.getPackageFragmentRoots()) {

			if (root.getKind() == IPackageFragmentRoot.K_SOURCE) {

				return root;
			}
		}

		throw new Exception("No source folder found.");
	}

	public static ICompilationUnit getActiveCompilationUnit() {

		try {

			IEditorPart editor = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();

			if (editor == null) {

				return null;
			}

			IEditorInput input = editor.getEditorInput();

			IFile file = (IFile) input.getAdapter(IFile.class);

			if (file == null) {

				return null;
			}

			return (ICompilationUnit) JavaCore.create(file);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

}