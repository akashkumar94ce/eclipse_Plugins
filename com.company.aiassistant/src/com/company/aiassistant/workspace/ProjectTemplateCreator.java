package com.company.aiassistant.workspace;

import org.eclipse.core.resources.IProject;

public final class ProjectTemplateCreator {

    private ProjectTemplateCreator() {
    }

    public static void createTemplate(
            IProject project,
            String projectType)
            throws Exception {

        if (projectType == null) {

            return;
        }

        if ("JAVA".equalsIgnoreCase(
                projectType)) {

            createJavaTemplate(
                    project);

            return;
        }

        if ("MAVEN".equalsIgnoreCase(
                projectType)) {

            createMavenTemplate(
                    project);

            return;
        }

        if ("SPRING_BOOT".equalsIgnoreCase(
                projectType)) {

            createSpringBootTemplate(
                    project);

            return;
        }
    }

    private static void createSpringBootTemplate(
            IProject project)
            throws Exception {

        WorkspaceManager.createWorkspaceFile(
                project,
                "pom.xml",
                MavenPomTemplates.getSpringBootPom(
                        project.getName()));

        WorkspaceManager.createWorkspaceFile(
                project,
                "src/main/resources/application.properties",
                "");

        WorkspaceManager.createWorkspaceFile(
                project,
                "src/test/java/.gitkeep",
                "");
    }

    private static String getPom() {

        return
            "<project xmlns=\"http://maven.apache.org/POM/4.0.0\">" +
            "</project>";
    }
    
    private static void createJavaTemplate(
            IProject project)
            throws Exception {

        // Nothing special for now.
    }
    
    private static void createMavenTemplate(
            IProject project)
            throws Exception {

        WorkspaceManager.createWorkspaceFile(
                project,
                "pom.xml",
                MavenPomTemplates.getMavenPom(
                        project.getName()));

        WorkspaceManager.createWorkspaceFile(
                project,
                "src/test/java/.gitkeep",
                "");
    }
}