package com.company.aiassistant.workspace;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;

import com.company.aiassistant.project.GeneratedFile;
import com.company.aiassistant.project.MultiFileResponseParser;
import com.company.aiassistant.project.ProjectGenerationResult;
import com.company.aiassistant.workspace.FileModificationExecutor;
import com.company.aiassistant.workspace.WorkspaceModificationParser;
import com.company.aiassistant.workspace.WorkspaceModificationResult;

/**
 * Executes parsed AI workspace actions.
 */
public final class WorkspaceExecutor {
	

    private WorkspaceExecutor() {
    }

    public static void execute(
            ProjectGenerationResult result)
            throws Exception {

        if (result == null) {

            throw new Exception(
                    "No generation result.");
        }

        String action =
                result.getActionType();

        if (action == null) {

            throw new Exception(
                    "No action type found.");
        }

        if (ProjectGenerationResult.ACTION_CREATE_FILE
                .equalsIgnoreCase(action)) {

            executeSingleFile(
                    result);

            return;
        }

        if (ProjectGenerationResult.ACTION_CREATE_FILES
                .equalsIgnoreCase(action)) {

        	IJavaProject javaProject =
        	        WorkspaceSelectionUtil
        	                .getActiveEditorProject();

        	if (javaProject == null) {

        	    javaProject =
        	            WorkspaceSelectionUtil
        	                    .getSelectedJavaProject();
        	}

        	if (javaProject == null) {

        	    throw new Exception(
        	            "Please select or open a project.");
        	}

        	executeMultiFile(
        	        result.getRawResponse(),
        	        javaProject.getProject());
            return;
        }

        if (ProjectGenerationResult.ACTION_CREATE_PROJECT
                .equalsIgnoreCase(action)) {

            executeCreateProject(
                    result);

            return;
        }
        
        if ("MODIFY_FILE"
                .equalsIgnoreCase(
                        action)) {

            executeModifyFile(
                    result);

            return;
        }

        throw new Exception(
                "Unsupported action: "
                        + action);
    }

    private static void executeSingleFile(
            ProjectGenerationResult result)
            throws Exception {

        List<GeneratedFile> files =
                result.getProject()
                      .getFiles();

        if (files.isEmpty()) {

            throw new Exception(
                    "No files found.");
        }

        GeneratedFile file =
                files.get(0);

        createFile(file);
    }


    private static void createFile(
            GeneratedFile file)
            throws Exception {

        String packageName =
                file.getPackageName();

        String fileName =
                file.getFileName();

        String content =
                file.getContent();

        if (packageName == null
                || packageName.trim()
                               .isEmpty()) {

            IPackageFragment selectedPackage =
                    WorkspaceSelectionUtil
                            .getSelectedPackage();

            if (selectedPackage == null) {

                throw new Exception(
                        "No package specified and no package selected.");
            }

            packageName =
                    selectedPackage
                            .getElementName();
        }

        IPackageFragment targetPackage =
                WorkspaceManager
                        .createPackage(
                                packageName);

        String className =
                fileName;

        if (className.endsWith(
                ".java")) {

            className =
                    className.substring(
                            0,
                            className.length()
                                    - 5);
        }

        ICompilationUnit unit =
                WorkspaceManager
                        .createJavaClass(
                                targetPackage,
                                className,
                                content);

        if (unit != null
                && unit.getResource() != null) {

            WorkspaceManager.openFile(
                    (org.eclipse.core.resources.IFile)
                            unit.getResource());
        }
    }
    
    private static void executeMultiFile(
            String response,
            IProject project)
            throws Exception {

        List<GeneratedFile> files =
                MultiFileResponseParser.parse(
                        response);

        if (files.isEmpty()) {

            throw new Exception(
                    "No files found.");
        }

        

        for (GeneratedFile file
                : files) {

        	String content =
        	        ensurePackageDeclaration(
        	                file);

        	IFile created =
        	        WorkspaceManager
        	                .createWorkspaceFile(
        	                        project,
        	                        file.getPath(),
        	                        content);
        	
           /* IFile created =
                    WorkspaceManager
                            .createWorkspaceFile(
                                    project,
                                    file.getPath(),
                                    file.getContent());*/

            WorkspaceManager.openFile(
                    created);
        }
    }

	private static void executeCreateProject(
	        ProjectGenerationResult result)
	        throws Exception {

	    String response =
	            result.getRawResponse();

	    String projectName =
	            extractValue(
	                    response,
	                    "PROJECT_NAME:");

	    if (projectName == null
	            || projectName.trim()
	                           .length() == 0) {

	        throw new Exception(
	                "PROJECT_NAME not found.");
	    }

	   /* IProject project =
	            ProjectCreator
	                    .createProject(
	                            projectName);*/
	    
	    IProject project = ProjectCreator.createProject(
							            result.getProject()
							                  .getProjectName(),
							            result.getProjectType());
	    
	    if (result.getProjectType()
	            != null) {

	        ProjectTemplateCreator
	                .createTemplate(
	                        project,
	                        result.getProjectType());
	    }

	    executeMultiFile(
	            response,
	            project);
	}
    
	private static String extractValue(
	        String text,
	        String key) {

	    int start =
	            text.indexOf(
	                    key);

	    if (start < 0) {

	        return "";
	    }

	    start += key.length();

	    int end =
	            text.indexOf(
	                    '\n',
	                    start);

	    if (end < 0) {

	        end =
	                text.length();
	    }

	    return text.substring(
	            start,
	            end)
	            .trim();
	}
 
	private static void executeModifyFile(
	        ProjectGenerationResult result)
	        throws Exception {

	    WorkspaceModificationResult mod =
	            WorkspaceModificationParser
	                    .parse(
	                            result.getRawResponse());

	    if (!"ADD_METHOD"
	            .equalsIgnoreCase(
	                    mod.getModificationType())) {

	        throw new Exception(
	                "Unsupported modification type: "
	                        + mod.getModificationType());
	    }

	    FileModificationExecutor
	            .addMethod(
	                    mod);
	}

	private static String ensurePackageDeclaration(
	        GeneratedFile file) {

	    String content =
	            file.getContent();

	    String path =
	            file.getPath();

	    if (content == null
	            || path == null) {

	        return content;
	    }

	    if (!path.endsWith(".java")) {

	        return content;
	    }

	    if (content.contains("package ")) {

	        return content;
	    }

	    String packageName =
	            derivePackageFromPath(
	                    path);

	    if (packageName == null
	            || packageName.length() == 0) {

	        return content;
	    }
	    
	    System.out.println(
	            "Injected package: "
	            + packageName);

	    return "package "
	            + packageName
	            + ";\n\n"
	            + content;
	}
	
	private static String derivePackageFromPath(
	        String path) {

	    String normalized =
	            path.replace(
	                    '\\',
	                    '/');

	    int start =
	            normalized.indexOf(
	                    "src/main/java/");

	    if (start < 0) {

	        return "";
	    }

	    String packagePart =
	            normalized.substring(
	                    start
	                    + "src/main/java/"
	                            .length());

	    int lastSlash =
	            packagePart.lastIndexOf(
	                    '/');

	    if (lastSlash < 0) {

	        return "";
	    }

	    packagePart =
	            packagePart.substring(
	                    0,
	                    lastSlash);

	    return packagePart.replace(
	            '/',
	            '.');
	}
}