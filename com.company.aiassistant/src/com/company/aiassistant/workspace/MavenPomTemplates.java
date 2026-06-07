package com.company.aiassistant.workspace;

/**
 * Centralized Maven and Spring Boot pom.xml templates.
 */
public final class MavenPomTemplates {

    private MavenPomTemplates() {
    }

    /**
     * Standard Maven Java project.
     */
    public static String getMavenPom(
            String projectName) {

        StringBuilder sb =
                new StringBuilder();

        sb.append(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

        sb.append(
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n");

        sb.append(
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");

        sb.append(
                " xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 ");

        sb.append(
                "https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");

        sb.append(
                "    <modelVersion>4.0.0</modelVersion>\n");

        sb.append(
                "    <groupId>com.company</groupId>\n");

        sb.append(
                "    <artifactId>")
          .append(projectName)
          .append("</artifactId>\n");

        sb.append(
                "    <version>1.0.0</version>\n");

        sb.append(
                "</project>");

        return sb.toString();
    }

    /**
     * Spring Boot Maven project.
     */
    public static String getSpringBootPom(
            String projectName) {

        StringBuilder sb =
                new StringBuilder();

        sb.append(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

        sb.append(
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n");

        sb.append(
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");

        sb.append(
                " xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 ");

        sb.append(
                "https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");

        sb.append(
                "    <modelVersion>4.0.0</modelVersion>\n");

        sb.append(
                "    <parent>\n");

        sb.append(
                "        <groupId>org.springframework.boot</groupId>\n");

        sb.append(
                "        <artifactId>spring-boot-starter-parent</artifactId>\n");

        sb.append(
                "        <version>3.3.0</version>\n");

        sb.append(
                "    </parent>\n");

        sb.append(
                "    <groupId>com.company</groupId>\n");

        sb.append(
                "    <artifactId>")
          .append(projectName)
          .append("</artifactId>\n");

        sb.append(
                "    <version>1.0.0</version>\n");

        sb.append(
                "    <properties>\n");

        sb.append(
                "        <java.version>17</java.version>\n");

        sb.append(
                "    </properties>\n");

        sb.append(
                "    <dependencies>\n");

        sb.append(
                "        <dependency>\n");

        sb.append(
                "            <groupId>org.springframework.boot</groupId>\n");

        sb.append(
                "            <artifactId>spring-boot-starter-web</artifactId>\n");

        sb.append(
                "        </dependency>\n");

        sb.append(
                "    </dependencies>\n");

        sb.append(
                "</project>");

        return sb.toString();
    }
}