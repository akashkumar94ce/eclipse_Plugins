package com.copilot.prompts;

import com.copilot.views.EditorContext;

public class PromptBuilder {

    public static final String OP_CHAT = "Chat (Freeform)";
    public static final String OP_EXPLAIN = "Explain Code";
    public static final String OP_SUMMARIZE = "Summarize Code";
    public static final String OP_FIX_BUG = "Fix Bugs";
    public static final String OP_ADD_CODE = "Add Code to File";
    public static final String OP_GENERATE_CODE = "Generate Code Fragment";
    public static final String OP_IMPLEMENT_FEATURE = "Implement Feature (Multi-File)"; // NEW OPERATION
    public static final String OP_CREATE_NAV_PLUGIN = "Create Navigator Plugin"; 
    public static final String OP_GENERATE_PROJECT = "Generate Complete Project Structure";

    public static String build(String operationType, String userQuery, EditorContext ctx) {
        StringBuilder sb = new StringBuilder();
        
        // 1. Inject Rich Workspace Context
        if (!OP_GENERATE_PROJECT.equals(operationType) && !OP_CREATE_NAV_PLUGIN.equals(operationType) && ctx != null) {
            sb.append("--- Current Workspace Context ---\n");
            if (ctx.getProjectName() != null) sb.append("Selected Project: ").append(ctx.getProjectName()).append("\n");
            if (ctx.getContainerPath() != null) sb.append("Package / Path: ").append(ctx.getContainerPath()).append("\n");
            if (ctx.getFileName() != null) sb.append("Active File: ").append(ctx.getFileName()).append("\n");
            
            // Send the FULL file content so the AI can safely modify it
            if (ctx.getFullFileContent() != null && !ctx.getFullFileContent().trim().isEmpty()) {
                sb.append("\n--- Entire Content of Active File ---\n```\n")
                  .append(ctx.getFullFileContent())
                  .append("\n```\n");
            }
            
            if (ctx.getSelectedText() != null && !ctx.getSelectedText().trim().isEmpty()) {
                sb.append("\n===== SPECIFICALLY SELECTED CODE =====\n")
                  .append(ctx.getSelectedText())
                  .append("\n===== END SELECTED CODE =====\n");
            }
            sb.append("---------------------------------\n\n");
        }

        // 2. Operation Specific Instructions
        if (OP_CHAT.equals(operationType)) {
            sb.append(userQuery);
        } 
        else if (OP_FIX_BUG.equals(operationType)) {
            sb.append("You are fixing ONLY the specifically selected code fragment.\n\n")
              .append("IMPORTANT RULES:\n")
              .append("1. Return ONLY the corrected version of the selected code.\n")
              .append("2. Use the full file context only to understand the surrounding logic.\n\n")
              .append("RESPONSE FORMAT:\n")
              .append("## FINAL_CODE\n```java\n<corrected code here>\n```\n\n")
              .append("Specific bug to fix: ").append(userQuery);
        }
        else if (OP_ADD_CODE.equals(operationType)) { 
            sb.append("You are generating a new code snippet to be inserted into the active file.\n\n")
              .append("IMPORTANT RULES:\n")
              .append("1. Generate ONLY the new code requested (e.g., the new method, field, or block).\n")
              .append("2. Do NOT generate the entire class or wrap the output in <file> tags.\n\n")
              .append("RESPONSE FORMAT:\n")
              .append("## FINAL_CODE\n```java\n<new code here>\n```\n\n")
              .append("Requirement: ").append(userQuery);
        }
        else if (OP_IMPLEMENT_FEATURE.equals(operationType)) { 
            sb.append("Act as an expert Java developer implementing a new feature across an EXISTING layered project.\n\n")
              .append("IMPORTANT RULES:\n")
              .append("1. COMPLETE END-TO-END WIRING: You MUST implement the feature across ALL applicable architectural layers. If the feature requires database access and web exposure, you must update the Repository, Service, ServiceImpl, AND the Controller files.\n")
              .append("2. CRITICAL XML FORMAT: For EVERY file modified or created, you MUST wrap the ENTIRE source code in structural XML tags. Example:\n")
              .append("<file path=\"src/com/demo/MyClass.java\">\npackage com.demo;\npublic class MyClass { ... }\n</file>\n")
              .append("3. NO PARTIAL UPDATES: When modifying an existing file, you MUST provide the FULL, complete, compilable source code. Do NOT use placeholders like `// ... existing code ...` because my parser will completely overwrite the file on disk.\n")
              .append("4. Review the 'Project Architecture Skeleton' below. It shows all existing files, classes, and methods. Use this to identify every file in the flow that needs to be updated to complete the feature.\n\n");
              
              if (ctx.getProjectSkeleton() != null && !ctx.getProjectSkeleton().isEmpty()) {
                  sb.append("--- Project Architecture Skeleton ---\n")
                    .append(ctx.getProjectSkeleton())
                    .append("-------------------------------------\n\n");
              }
              
              sb.append("Requirement: ").append(userQuery);
        }
        else if (OP_GENERATE_CODE.equals(operationType)) {
            sb.append("Act as an expert Java developer adding new files/code to an EXISTING project.\n")
              .append("Requirement: ").append(userQuery).append("\n\n")
              .append("IMPORTANT RULES:\n")
              .append("1. Analyze the 'Package / Path' in the Current Workspace Context above.\n")
              .append("2. INFER THE PROJECT TYPE: If the path contains 'src/main/java', treat it as a Maven/Spring Boot project. If it is just 'src/', treat it as a standard Java project.\n")
              .append("3. CRITICAL XML FORMAT: Wrap every generated file in structural XML tags using the exact relative path. Example:\n")
              .append("<file path=\"src/com/demo/MyClass.java\">\npackage com.demo;\npublic class MyClass {}\n</file>\n");
        }
        else if (OP_CREATE_NAV_PLUGIN.equals(operationType)) {
            sb.append("Act as an expert IBM FileNet and Content Navigator (ICN) developer.\n")
              .append("Requirement: ").append(userQuery).append("\n\n")
              .append("IMPORTANT RULES:\n")
              .append("1. Generate a complete ICN Plugin project structure using Ant (NOT Maven).\n")
              .append("2. You MUST strictly adhere to the standard ICN layout:\n")
              .append("   - Main Java plugin class in `src/com/ibm/ecm/extension/yourplugin/`\n")
              .append("   - JavaScript/CSS/HTML in `src/com/ibm/ecm/extension/yourplugin/WebContent/`\n")
              .append("   - Dojo widgets in `src/com/ibm/ecm/extension/yourplugin/WebContent/yourPluginDojo/`\n")
              .append("   - Ant build script at the root: `build.xml`\n")
              .append("   - Manifest at `META-INF/MANIFEST.MF`\n")
              .append("3. CRITICAL XML FORMAT: Wrap EVERY generated file in structural XML tags using the exact relative path. Example:\n\n")
              .append("<file path=\"src/com/ibm/ecm/extension/sample/SamplePlugin.java\">\npackage com.ibm.ecm.extension.sample;\npublic class SamplePlugin extends com.ibm.ecm.extension.Plugin {}\n</file>\n")
              .append("<file path=\"build.xml\">\n\n</file>\n\n")
              .append("Do not skip files. Provide complete, compilable code for the Action, Plugin, and Dojo module.");
        }
        else if (OP_GENERATE_PROJECT.equals(operationType)) {
            sb.append("Act as an expert Eclipse Java architectural generator.\n")
              .append("Requirement: ").append(userQuery).append("\n\n")
              .append("IMPORTANT RULES:\n")
              .append("1. Generate a COMPLETE, production-ready project framework from scratch.\n")
              .append("2. CRITICAL XML FORMAT: You must wrap EVERY SINGLE FILE inside structural XML tags so my IDE can parse it. Example:\n\n")
              .append("<file path=\"src/main/java/com/demo/Application.java\">\npackage com.demo;\npublic class Application {}\n</file>\n")
              .append("<file path=\"pom.xml\">\n\n</file>\n\n")
              .append("Do not skip files. Do not use markdown code blocks outside of the <file> tags.");
        } 
        else {
            sb.append("Process the following request explicitly: ").append(userQuery);
        }

        return sb.toString();
    }
}