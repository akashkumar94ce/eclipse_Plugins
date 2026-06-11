package com.copilot.project;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.*;

public class ProjectGenerator {

    public static void buildFromAiResponse(String projectName, String aiResponse, IProgressMonitor monitor) throws CoreException {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject project = root.getProject(projectName);
        
        // Check if the project already exists before we try to recreate it
        boolean projectExists = project.exists();
        if (!projectExists) {
            project.create(monitor);
        }
        project.open(monitor);

        // 1. Detect project nature based on AI output
        boolean isMavenStructure = aiResponse.contains("<file path=\"pom.xml\">") || aiResponse.contains("src/main/java");

        // 2. CRITICAL FIX: Only configure Natures and Classpath if it's a NEW project 
        // This prevents the "Cannot nest src/main/java inside src" exception on existing projects.
        if (!project.hasNature(JavaCore.NATURE_ID)) {
            IProjectDescription description = project.getDescription();
            description.setNatureIds(new String[] { JavaCore.NATURE_ID });
            project.setDescription(description, monitor);

            IJavaProject javaProject = JavaCore.create(project);
            List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
            
            // Add default JRE library
            entries.add(JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER")));

            if (isMavenStructure) {
                String[] mavenFolders = {"src/main/java", "src/main/resources", "src/test/java", "src/test/resources"};
                for (String srcName : mavenFolders) {
                    if (aiResponse.contains("path=\"" + srcName) || srcName.equals("src/main/java")) {
                        IFolder sFolder = project.getFolder(new Path(srcName));
                        if (!sFolder.exists()) createFolderTree(project, srcName, monitor);
                        entries.add(JavaCore.newSourceEntry(sFolder.getFullPath()));
                    }
                }
                IFolder outputFolder = project.getFolder(new Path("target/classes"));
                if (!outputFolder.exists()) createFolderTree(project, "target/classes", monitor);
                javaProject.setOutputLocation(outputFolder.getFullPath(), monitor);
            } else {
                IFolder sFolder = project.getFolder(new Path("src"));
                if (!sFolder.exists()) createFolderTree(project, "src", monitor);
                entries.add(JavaCore.newSourceEntry(sFolder.getFullPath()));
                
                IFolder outputFolder = project.getFolder(new Path("bin"));
                if (!outputFolder.exists()) createFolderTree(project, "bin", monitor);
                javaProject.setOutputLocation(outputFolder.getFullPath(), monitor);
            }

            javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[0]), monitor);
        }

        // 3. Parse and physically write the files
        String searchTag = "<file path=\"";
        int index = aiResponse.indexOf(searchTag);

        while (index != -1) {
            int pathStart = index + searchTag.length();
            int pathEnd = aiResponse.indexOf("\"", pathStart);
            if (pathEnd == -1) break;

            String relativeFilePath = aiResponse.substring(pathStart, pathEnd);
            
            int contentStart = aiResponse.indexOf(">", pathEnd) + 1;
            int contentEnd = aiResponse.indexOf("</file>", contentStart);
            if (contentEnd == -1) break;

            String fileContents = aiResponse.substring(contentStart, contentEnd).trim();

            // Strip out markdown code blocks generated inside the XML tags
            if (fileContents.startsWith("```")) {
                int firstBreak = fileContents.indexOf("\n");
                if (firstBreak != -1) fileContents = fileContents.substring(firstBreak).trim();
                if (fileContents.endsWith("```")) fileContents = fileContents.substring(0, fileContents.length() - 3).trim();
            }

            createWorkspaceFile(project, relativeFilePath, fileContents, monitor);
            index = aiResponse.indexOf(searchTag, contentEnd);
        }
        
        // Refresh UI so the new files appear immediately in the Package Explorer
        project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
    }

    private static void createWorkspaceFile(IProject project, String relativePath, String content, IProgressMonitor monitor) throws CoreException {
        IFile file = project.getFile(new Path(relativePath));
        IContainer parentContainer = file.getParent();
        
        if (!parentContainer.exists() && parentContainer instanceof IFolder) {
            createFolderTree(project, parentContainer.getProjectRelativePath().toString(), monitor);
        }

        ByteArrayInputStream sourceStream = new ByteArrayInputStream(content.getBytes());
        if (file.exists()) {
            file.setContents(sourceStream, IResource.FORCE, monitor);
        } else {
            file.create(sourceStream, true, monitor);
        }
    }

    private static IFolder createFolderTree(IProject project, String folderPath, IProgressMonitor monitor) throws CoreException {
        String[] folders = folderPath.split("/");
        IFolder current = null;
        StringBuilder path = new StringBuilder();

        for (String folder : folders) {
            if (path.length() > 0) path.append("/");
            path.append(folder);
            
            current = project.getFolder(new Path(path.toString()));
            if (!current.exists()) {
                current.create(true, true, monitor);
            }
        }
        return current;
    }
}