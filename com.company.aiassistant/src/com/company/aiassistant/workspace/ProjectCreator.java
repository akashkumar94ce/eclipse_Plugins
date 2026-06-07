package com.company.aiassistant.workspace;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public final class ProjectCreator {

    private ProjectCreator() {
    }

    public static IProject createProject(
            String projectName,
            String projectType)
            throws Exception {

        IProject project =
                ResourcesPlugin
                        .getWorkspace()
                        .getRoot()
                        .getProject(
                                projectName);

        if (project.exists()) {

            throw new Exception(
                    "Project already exists: "
                            + projectName);
        }

        project.create(
                new NullProgressMonitor());

        project.open(
                new NullProgressMonitor());

        /*
         * Convert to Java Project
         */
        IJavaProject javaProject =
                JavaCore.create(
                        project);

        org.eclipse.core.resources.IProjectDescription desc =
                project.getDescription();

        desc.setNatureIds(
                new String[] {
                        JavaCore.NATURE_ID
                });

        project.setDescription(
                desc,
                new NullProgressMonitor());

        /*
         * Create src folder
         */
     /*   IFolder srcFolder =
                project.getFolder(
                        "src");

        srcFolder.create(
                true,
                true,
                new NullProgressMonitor());*/
        
        String sourceFolderPath =
                getSourceFolder(
                        projectType);

        IFolder sourceFolder =
                createFolderTree(
                        project,
                        sourceFolderPath);

        /*
         * Create bin folder
         */
        IFolder binFolder =
                project.getFolder(
                        "bin");

        binFolder.create(
                true,
                true,
                new NullProgressMonitor());

        javaProject.setOutputLocation(
                binFolder.getFullPath(),
                new NullProgressMonitor());

        /*
         * JRE Library
         */
        IClasspathEntry jreEntry =
                JavaCore.newContainerEntry(
                        new org.eclipse.core.runtime.Path(
                                "org.eclipse.jdt.launching.JRE_CONTAINER"));

        /*
         * Source Folder
         */
        /*IClasspathEntry sourceEntry =
                JavaCore.newSourceEntry(
                        srcFolder.getFullPath());*/
        
        IClasspathEntry sourceEntry =
                JavaCore.newSourceEntry(
                        sourceFolder.getFullPath());

        javaProject.setRawClasspath(
                new IClasspathEntry[] {
                        sourceEntry,
                        jreEntry
                },
                new NullProgressMonitor());

        project.refreshLocal(
                IProject.DEPTH_INFINITE,
                new NullProgressMonitor());

        return project;
    }
    
    private static IFolder createFolderTree(
            IProject project,
            String folderPath)
            throws Exception {

        String[] folders =
                folderPath.split("/");

        IFolder current =
                null;

        StringBuilder path =
                new StringBuilder();

        for (int i = 0;
             i < folders.length;
             i++) {

            if (path.length() > 0) {

                path.append("/");
            }

            path.append(
                    folders[i]);

            current =
                    project.getFolder(
                            path.toString());

            if (!current.exists()) {

                current.create(
                        true,
                        true,
                        new NullProgressMonitor());
            }
        }

        return current;
    }
    
    private static String getSourceFolder(
            String projectType) {

        if ("SPRING_BOOT".equalsIgnoreCase(
                projectType)) {

            return "src/main/java";
        }

        if ("SPRING_MVC".equalsIgnoreCase(
                projectType)) {

            return "src/main/java";
        }

        if ("REST_API".equalsIgnoreCase(
                projectType)) {

            return "src/main/java";
        }

        if ("MAVEN".equalsIgnoreCase(
                projectType)) {

            return "src/main/java";
        }

        if ("WEB_APP".equalsIgnoreCase(
                projectType)) {

            return "src";
        }

        return "src";
    }
}