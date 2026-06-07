package com.company.aiassistant.ai;

import com.company.aiassistant.context.CodeContext;
import com.company.aiassistant.context.ContextBuilder;
import com.company.aiassistant.context.ContextExtractor;
import com.company.aiassistant.workspace.WorkspaceContext;
import com.company.aiassistant.workspace.WorkspaceContextExtractor;
import com.company.aiassistant.workspace.WorkspaceContextFormatter;

public final class PromptBuilder {

    private PromptBuilder() {
    }

    /**
     * Main entry point used by AIServiceImpl.
     */
    public static String build(
            AIRequest request) {

        if (request == null) {
            return "";
        }

        AIActionType actionType =
                request.getActionType();

        if (actionType == null) {

            return request.getPrompt();
        }

        switch (actionType) {

        case CHAT:

            return request.getPrompt();

        case EXPLAIN:

            return buildExplainPrompt(
                    request.getSelectedCode());

        case IMPROVE:

            return buildImprovePrompt(
                    request.getSelectedCode());

        case FIX_BUG:

            return buildFixBugPrompt(
                    request.getSelectedCode());

        case GENERATE_CODE:

            return buildGeneratePrompt(
                    request.getPrompt());
            
        case GENERATE_PROJECT:
            return buildGenerateProjectPrompt(
                    request.getPrompt());

        default:

            return request.getPrompt();
        }
    }

    /**
     * Explain code prompt.
     */
    public static String buildExplainPrompt(
            String code) {

        StringBuilder sb =
                new StringBuilder();

        sb.append(
                "Explain the following code in detail.\n\n");

        sb.append(
                "Include:\n");

        sb.append(
                "1. Purpose of the code\n");

        sb.append(
                "2. Line by line explanation\n");

        sb.append(
                "3. Important logic\n");

        sb.append(
                "4. Possible issues\n");

        sb.append(
                "5. Suggestions for improvement\n\n");

        sb.append(
                buildContextSection());

        sb.append(
                "Code:\n");

        sb.append(code);

        return sb.toString();
    }

    /**
     * Improve code prompt.
     */
    public static String buildImprovePrompt(
            String code) {

        StringBuilder sb =
                new StringBuilder();

        
        /*sb.append(
        	    "IMPORTANT RESPONSE FORMAT:\n\n");

        	sb.append(
        	    "Return the improved code FIRST.\n");

        	sb.append(
        	    "The FIRST code block must contain ONLY the final refactored code.\n");

        	sb.append(
        	    "After that explain improvements.\n\n");*/
        
        sb.append(
        	    "You are improving ONLY the selected code fragment.\n\n");

        	sb.append(
        	    "IMPORTANT RULES:\n");

        	sb.append(
        	    "1. Do NOT generate the entire class.\n");

        	sb.append(
        	    "2. Do NOT generate the entire method unless the full method was selected.\n");

        	sb.append(
        	    "3. Improve ONLY the selected code.\n");

        	sb.append(
        	    "4. Use surrounding context only for understanding.\n");

        	sb.append(
        	    "5. Return ONLY the improved version of the selected code.\n");

        	sb.append(
        	    "6. Preserve behavior unless an improvement is necessary.\n\n");

        	sb.append(
        	    "RESPONSE FORMAT:\n\n");

        	sb.append(
        	    "## IMPROVED_CODE\n");

        	sb.append(
        	    "```java\n");

        	sb.append(
        	    "<improved selected code only>\n");

        	sb.append(
        	    "```\n\n");

        	sb.append(
        	    "## IMPROVEMENTS\n");

        	sb.append(
        	    "<explanation>\n\n");

        sb.append(
                buildContextSection());

        /*sb.append(
                "Code:\n");

        sb.append(code);*/
        
        sb.append(
        	    "\n===== SELECTED CODE =====\n");

        	sb.append(code);

        	sb.append(
        	    "\n===== END SELECTED CODE =====\n\n");

        return sb.toString();
    }

    /**
     * Fix bug prompt.
     */
    public static String buildFixBugPrompt(
            String code) {

        StringBuilder sb =
                new StringBuilder();

        
       /* sb.append(
        	    "IMPORTANT RESPONSE FORMAT:\n\n");

        	sb.append(
        	    "Return the corrected code FIRST.\n");

        	sb.append(
        	    "The FIRST code block must contain ONLY the final corrected code.\n");

        	sb.append(
        	    "Do not show original code in any code block before the corrected code.\n");

        	sb.append(
        	    "After the first code block, explain the bugs and fixes.\n\n");

        	sb.append(
        	    "Response example:\n\n");

        	sb.append(
        	    "## FINAL_CODE\n");

        	sb.append(
        	    "```java\n");

        	sb.append(
        	    "// corrected code here\n");

        	sb.append(
        	    "```\n\n");

        	sb.append(
        	    "## EXPLANATION\n");

        	sb.append(
        	    "- bug 1\n");

        	sb.append(
        	    "- bug 2\n\n");*/
        
        sb.append(
        	    "You are fixing ONLY the selected code fragment.\n\n");

        	sb.append(
        	    "IMPORTANT RULES:\n");

        	sb.append(
        	    "1. Do NOT generate the entire class.\n");

        	sb.append(
        	    "2. Do NOT generate the entire method.\n");

        	sb.append(
        	    "3. Do NOT generate surrounding code.\n");

        	sb.append(
        	    "4. Return ONLY the corrected version of the selected code.\n");

        	sb.append(
        	    "5. Preserve original formatting whenever possible.\n");

        	sb.append(
        	    "6. Use surrounding context only to understand the code.\n");

        	sb.append(
        	    "7. If the selected code is a single line, return only the corrected line.\n");

        	sb.append(
        	    "8. If the selected code is a block, return only the corrected block.\n\n");

        	sb.append(
        	    "RESPONSE FORMAT:\n\n");

        	sb.append(
        	    "## FINAL_CODE\n");

        	sb.append(
        	    "```java\n");

        	sb.append(
        	    "<corrected selected code only>\n");

        	sb.append(
        	    "```\n\n");

        	sb.append(
        	    "## EXPLANATION\n");

        	sb.append(
        	    "<explanation>\n\n");

        sb.append(
                buildContextSection());

     /*   sb.append(
                "Code:\n");

        sb.append(code);*/
        
        sb.append(
        	    "\n===== SELECTED CODE =====\n");

        	sb.append(code);

        	sb.append(
        	    "\n===== END SELECTED CODE =====\n\n");

        return sb.toString();
    }

    public static String buildGeneratePrompt(
            String requirement) {

        StringBuilder sb =
                new StringBuilder();

        sb.append(
                "You are an Eclipse Java development assistant.\n\n");

        sb.append(
                "IMPORTANT RULES:\n");

        sb.append(
                "1. If user requests a new class, return CREATE_FILE format.\n");

        sb.append(
                "2. If user requests multiple files, return CREATE_PROJECT format.\n");

        sb.append(
                "3. Always generate production quality code.\n");

        sb.append(
                "4. Add imports.\n");

        sb.append(
                "5. Add inline comments where useful.\n");

        sb.append(
                "6. Use proper package names.\n");

        sb.append(
                "7. Never return explanations before generated files.\n");

        sb.append(
                "8. File content must be complete and compilable.\n\n");

        sb.append(
                "CREATE_FILE format example:\n\n");

        sb.append(
                "PROJECT_ACTION: CREATE_FILE\n");

        sb.append(
                "PACKAGE: com.example.demo\n");

        sb.append(
                "FILE_NAME: UserService.java\n\n");

        sb.append(
                "```java\n");

        sb.append(
                "package com.example.demo;\n");

        sb.append(
                "public class UserService {}\n");

        sb.append(
                "```\n\n");

        sb.append(
                "CREATE_PROJECT format example:\n\n");

        sb.append(
                "PROJECT_ACTION: CREATE_PROJECT\n");

        sb.append(
                "PROJECT_NAME: DemoProject\n\n");

        sb.append(
                "FILE_START\n");

        sb.append(
                "PATH: src/main/java/com/demo/User.java\n");

        sb.append(
                "FILE_TYPE: JAVA\n\n");

        sb.append(
                "package com.demo;\n");

        sb.append(
                "public class User {}\n");

        sb.append(
                "FILE_END\n\n");
        
        sb.append(
                "MODIFICATION format example:\n\n");

        sb.append(
                "PROJECT_ACTION: MODIFY_FILE\n");

        sb.append(
                "TARGET_FILE: EmployeeService.java\n");

        sb.append(
                "MODIFICATION_TYPE: ADD_METHOD\n");

        sb.append(
                "METHOD_SIGNATURE: saveEmployee(Employee employee)\n\n");

        sb.append(
                "For interfaces:\n\n");

        sb.append(
                "```java\n");

        sb.append(
                "Employee saveEmployee(Employee employee);\n");

        sb.append(
                "```\n\n");

        sb.append(
                "For classes:\n\n");

        sb.append(
                "```java\n");

        sb.append(
                "public Employee saveEmployee(Employee employee) {\n");

        sb.append(
                "    return repository.save(employee);\n");

        sb.append(
                "}\n");

        sb.append(
                "```\n\n");

        /*sb.append(
                buildContextSection());
        
        sb.append(
                buildWorkspaceContext());*/
        
        sb.append(
                buildContextSection());

        sb.append(
                "\n===== TYPE RULES =====\n\n");

        sb.append(
                "If Type = interface:\n");

        sb.append(
                "1. Generate method declarations only.\n");

        sb.append(
                "2. Do NOT generate method body.\n");

        sb.append(
                "3. Do NOT use braces.\n");

        sb.append(
                "4. Example:\n");

        sb.append(
                "Employee saveEmployee(Employee employee);\n\n");

        sb.append(
                "If Type = class:\n");

        sb.append(
                "1. Generate complete implementation.\n");

        sb.append(
                "2. Generate method body.\n");

        sb.append(
                "3. Use available fields and dependencies.\n");

        sb.append(
                "4. Example:\n");

        sb.append(
                "public Employee saveEmployee(Employee employee) {\n");

        sb.append(
                "    return employeeRepository.save(employee);\n");

        sb.append(
                "}\n\n");

        sb.append(
                buildWorkspaceContext());

        sb.append(
                "\nUSER REQUIREMENT:\n");

        sb.append(
                requirement);

        return sb.toString();
    }
    
    /**
     * Build editor context section.
     */
    private static String buildContextSection() {

        try {

            CodeContext context =
                    ContextExtractor.extract();

            return ContextBuilder.buildContext(
                    context);

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }
    }
    
    private static String buildWorkspaceContext() {

        try {

            WorkspaceContext context =
                    WorkspaceContextExtractor
                            .extract();

            return WorkspaceContextFormatter
                    .format(
                            context);

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }
    }
    
    public static String buildGenerateProjectPrompt(
            String requirement) {

        StringBuilder sb =
                new StringBuilder();

        sb.append(
                "You are an Eclipse project generation assistant.\n\n");

        sb.append(
                "IMPORTANT RULES:\n");

        sb.append(
                "1. Determine project type.\n");

        sb.append(
                "2. Return PROJECT_TYPE.\n");

        sb.append(
                "3. Generate complete project structure.\n");

        sb.append(
                "4. Generate production quality code.\n");

        sb.append(
                "5. Never return explanations before project definition.\n\n");

        sb.append(
                "Supported PROJECT_TYPE values:\n");

        sb.append(
                "JAVA\n");

        sb.append(
                "MAVEN\n");

        sb.append(
                "SPRING_BOOT\n");

        sb.append(
                "SPRING_MVC\n");

        sb.append(
                "REST_API\n");

        sb.append(
                "WEB_APP\n\n");

        sb.append(
                "Response format:\n\n");

        sb.append(
                "PROJECT_ACTION: CREATE_PROJECT\n");

        sb.append(
                "PROJECT_NAME: DemoProject\n");

        sb.append(
                "PROJECT_TYPE: SPRING_BOOT\n\n");

        sb.append(
                "FILE_START\n");

        sb.append(
                "PATH: src/main/java/com/example/demo/Application.java\n");

        sb.append(
                "FILE_TYPE: JAVA\n\n");

        sb.append(
                "package com.example.demo;\n");

        sb.append(
                "public class Application {}\n");

        sb.append(
                "FILE_END\n\n");

        sb.append(
                "USER REQUIREMENT:\n\n");

        sb.append(
                requirement);

        return sb.toString();
    }
}