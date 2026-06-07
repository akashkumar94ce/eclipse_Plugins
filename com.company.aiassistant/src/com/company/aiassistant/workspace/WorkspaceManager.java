package com.company.aiassistant.workspace;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public final class WorkspaceManager {

    private WorkspaceManager() {
    }

    /**
     * Create package.
     */
    public static IPackageFragment createPackage(
            String packageName)
            throws Exception {

        IJavaProject project =
                WorkspaceSelectionUtil
                        .getActiveEditorProject();

        if (project == null) {

            project =
                    WorkspaceSelectionUtil
                            .getSelectedJavaProject();
        }

        if (project == null) {

            throw new Exception(
                    "Please open a file inside the target project.");
        }

        IPackageFragmentRoot root =
                WorkspaceSelectionUtil
                        .findSourceRoot(
                                project);

        IPackageFragment existing =
                root.getPackageFragment(
                        packageName);

        if (existing.exists()) {

            return existing;
        }

        return root.createPackageFragment(
                packageName,
                true,
                new NullProgressMonitor());
    }

    /**
     * Create Java class.
     */
    public static ICompilationUnit createJavaClass(
            IPackageFragment pkg,
            String className,
            String source)
            throws Exception {

        String fileName =
                className + ".java";

        return pkg.createCompilationUnit(
                fileName,
                source,
                true,
                new NullProgressMonitor());
    }

    /**
     * Create generic file.
     */
    public static IFile createFile(
            IPackageFragment pkg,
            String fileName,
            String content)
            throws Exception {

        IFile file =
                (IFile) pkg.getResource();

        IFile targetFile =
                file.getParent()
                    .getFile(
                            new org.eclipse.core.runtime.Path(
                                    fileName));

        ByteArrayInputStream stream =
                new ByteArrayInputStream(
                        content.getBytes("UTF-8"));

        if (targetFile.exists()) {

            targetFile.setContents(
                    stream,
                    true,
                    false,
                    new NullProgressMonitor());

        } else {

            targetFile.create(
                    stream,
                    true,
                    new NullProgressMonitor());
        }

        return targetFile;
    }

    /**
     * Open file in editor.
     */
    public static void openFile(
            IFile file) {

        try {

            IEditorPart editor =
                    IDE.openEditor(
                            PlatformUI
                                    .getWorkbench()
                                    .getActiveWorkbenchWindow()
                                    .getActivePage(),
                            file);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    public static IFile createWorkspaceFile(
            IProject project,
            String relativePath,
            String content)
            throws Exception {

        String[] parts =
                relativePath.split("/");

        org.eclipse.core.resources.IContainer parent =
                project;

        for (int i = 0;
             i < parts.length - 1;
             i++) {

            org.eclipse.core.resources.IFolder folder =
                    parent.getFolder(
                            new org.eclipse.core.runtime.Path(
                                    parts[i]));

            if (!folder.exists()) {

                folder.create(
                        true,
                        true,
                        new NullProgressMonitor());
            }

            parent = folder;
        }

        IFile file =
                parent.getFile(
                        new org.eclipse.core.runtime.Path(
                                parts[parts.length - 1]));

        ByteArrayInputStream stream =
                new ByteArrayInputStream(
                        content.getBytes("UTF-8"));

        if (file.exists()) {

            file.setContents(
                    stream,
                    true,
                    false,
                    new NullProgressMonitor());

        } else {

            file.create(
                    stream,
                    true,
                    new NullProgressMonitor());
        }

        return file;
    }
}