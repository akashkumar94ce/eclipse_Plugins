# Eclipse AI Co-pilot — User Manual & README

Welcome to your **Eclipse AI Co-pilot** extension workspace! This guide provides a comprehensive overview of your plugin's features, architectural behavior, installation procedures, and sample walkthrough instructions to maximize your coding productivity.

---

## 1. Overview & System Requirements

The Eclipse AI Co-pilot brings the cutting-edge capabilities of modern generative AI (like ChatGPT, Gemini, and local Ollama models) directly into the legacy developer workspace. It features real-time **Abstract Syntax Tree (AST) Project Skeleton Parsing**, isolated background transactional threading, smart code overwriting/scaffolding engine mechanics, and a responsive web-styled workspace layout.

### Technical Compatibility Matrix

* **Target IDE Platform:** Eclipse Kepler (4.3) and higher
* **Java Runtime Environment:** Java SE 7 or Java SE 8 (Optimized for legacy Oracle/IBM runtime stacks)
* **Underlying Core Natures Supported:** Core JDT (Java Development Tools), Apache Ant Build Lifecycles, Maven M2E execution topologies, and Spring Boot framework extensions.
* **External Core Bundles Required:** Google Gson (2.8.5+) added to plugin bundle path.

---

## 2. Installation & Sandbox Deployment

### Folder Structure Configuration

To verify the plugin bundle configuration locally, ensure your plugin source project matches this layout within the Eclipse development workspace:

```text
com.copilot.eclipse/
├── META-INF/
│   └── MANIFEST.MF          # Defines execution dependencies & Gson classpath mapping
├── lib/
│   └── gson-2.8.5.jar       # JSON parsing engine jar dependency
├── src/
│   └── com/copilot/
│       ├── Activator.java   # Bundle execution supervisor lifecycle controller
│       ├── network/
│       │   └── AIClient.java      # Connects HttpURLConnection payloads
│       ├── preferences/
│       │   ├── CopilotPreferencePage.java  # Responsive preference page manager
│       │   ├── PreferenceConstants.java    # Key constant tracking bindings
│       │   └── PreferenceInitializer.java  # Default parameters initializer
│       ├── project/
│       │   └── ProjectGenerator.java       # Disk I/O multi-file router engine
│       ├── prompts/
│       │   └── PromptBuilder.java          # Context assembly & system prompt system
│       └── views/
│           ├── CopilotView.java            # Render UI, state-locks, and layout control
│           └── EditorContext.java          # Workspace metadata transfer entity object
├── build.properties         # Maps resource outputs for compilation packaging
└── plugin.xml               # Registers views and preference configuration trees

```

### Setup Steps

1. Download **Gson 2.8.5 JAR** and place it within the `lib/` directory. Right-click the jar and select **Build Path -> Add to Build Path**.
2. Right-click the root plugin project folder (`com.copilot.eclipse`), then navigate to **Run As -> Eclipse Application**.
3. A clean sandbox runtime instance of Eclipse will launch automatically.

### Configuring AI Engine Connections

1. Inside the sandbox workbench window, navigate to the top toolbar menu and select **Window -> Preferences**.
2. Expand the sidebar menu and select **AI Co-pilot Configuration**.
3. **Select AI Provider:** Choose between `ChatGPT`, `Gemini`, `Ollama`, `AWS Bedrock`, `Grok`, or `Custom`.
* *Note:* Selecting `Ollama` automatically defaults the endpoint to local network scopes (`http://localhost:11434/api/generate`) and safely suspends API-key security validation checks.


4. Input your **Secret API Key**, target **Endpoint URL**, and specific **Model ID** matching your engine (e.g., `gpt-4o`, `gemini-1.5-pro`, or `llama3`). Click **Apply and Close**.

---

## 3. UI Layout Overview

To open the chat window, click **Window -> Show View -> Other...**, expand the **AI Co-pilot** category, and choose **Co-pilot Chat Window**.

```
+-------------------------------------------------------------+
|                                                             |
|  [✦ Welcome Timeline Area]                                  |
|  Displays interactive conversations using a web layout.    |
|  - User prompts appear in a light-blue bubble.              |
|  - AI responses appear in sharp white blocks.               |
|  - Full source code modules appear in styled dark mode code  |
|    blocks for excellent legibility.                         |
|                                                             |
+-------------------------------------------------------------+
| [ Dropdown Selection Menu (Choose Operation Action)       v] |
+-------------------------------------------------------------+
|                                                             |
|  [ Multiline Input Area TextBox Field Input Box           ] |
|                                                             |
+-------------------------------------------------------------+
|                                 [ Send ] [ Apply to Editor ]|
+-------------------------------------------------------------+

```

### Safety Operational Lock-outs

* **Thinking State Indicator:** When you click **Send**, the UI automatically locks out text input, grays out the execution control buttons, and renders an animated `✦ Thinking...` indicator pinned to the base of the scrolling viewport page timeline. This stops accidental dual-firing transactions.
* **Anti-Spam Apply Protections:** Clicking **Apply to Editor/Project** disables the button instantly to prevent duplicate generation streams from being written to your workspace files. The view re-enables control only when a new response transaction cycle successfully resolves.

---

## 4. Features & Sample Prompts Directory

### 1. Chat (Freeform)

* **What it does:** Provides general conversations. It automatically reads your background workspace metadata (active file name, parent package location, project bounds, and text selections) so you can converse without typing out contextual definitions manually.
* **Sample Prompt:** > *"How do I parse a date string formatted like yyyy-MM-dd into a Java Util Date object safely given our current workspace configuration constraints?"*

### 2. Explain Code

* **What it does:** Generates a high-level breakdown, architectural purpose, line-by-line functional tracing map, performance scaling traps, and optimization recommendations for your highlighted code.
* **Sample Action:** Highlight a complex database transaction block or a recursive algorithm block in your editor, change the dropdown selection to `Explain Code`, leave the text box blank, and press **Send**.

### 3. Summarize Code

* **What it does:** condenses heavy classes or long methods into three clear high-level bullet points detailing dependencies and business logic behaviors.
* **Sample Action:** Highlight a dense enterprise class constructor setup, select `Summarize Code`, and click **Send**.

### 4. Fix Bugs

* **What it does:** Analyzes logical bugs or runtime compiler warnings in your selection. It uses high-priority instructions to isolate edits, forcing the AI to return **only** the fixed target fragment inside a strict `## FINAL_CODE` wrapper.
* **How to use:** Highlight a compilation error or runtime edge case, select `Fix Bugs`, describe the error signature in the box (e.g., *"Getting a NullPointerException here"*), and click **Send**. Click **Apply to Editor/Project** to overwrite only the buggy lines.

### 5. Add Code to File

* **What it does:** Injects new methods or logic without requiring text selection or overwriting your active codebase blocks.
* **How to use:** Place your cursor on a blank line inside an existing class where you want to insert a feature, select `Add Code to File`, enter your prompt instruction, and click **Send**.
* **Sample Prompt:**
> *"Create a public static integer utility method named calculateTax that accepts a BigDecimal amount and applies an 18 percent calculation check."*


* **Applying the fix:** Clicking **Apply to Editor/Project** directly inserts the new method block right at your flashing blinking cursor location.

### 6. Generate Code Fragment

* **What it does:** Creates an isolated structural module (like a new class or interface component) within your active project scope. The system reads your path structure (e.g., `src/main/java`) to automatically determine if it should build a Maven layer or a standard Java project package declaration.
* **Sample Prompt:**
> *"Create a comprehensive User POJO class with data variables tracking id, login handle, creation timestamp, and full name with string serializations."*


* **Applying the fix:** Click **Apply to Editor/Project**, and the plugin automatically identifies your active project workspace and creates the new `.java` file with the correct package structure.

### 7. Implement Feature (Multi-File)

* **What it does:** Connects complex operations across multiple existing layers. It triggers the **Real-time Project Skeleton Map scanner** using JDT compiler tokens to analyze every class, package, and method signature in your project.
* **Sample Prompt:**
> *"Add a feature to delete an employee by email. Wire this up end-to-end throughout our standard architecture layers."*


* **Applying the fix:** The AI reads the skeleton structure, notices missing linkages, and outputs the full code changes for your Repository, Service, ServiceImpl, and Web Controller. Click **Apply to Editor/Project**, and the plugin updates all four layers simultaneously on your drive.

### 8. Create Navigator Plugin

* **What it does:** Scaffolds an IBM Content Navigator (ICN) enterprise extension plug-in project layout that maps perfectly to standard corporate development standards.
* **Sample Prompt:**
> *"Create a complete project for ibm content navigator plugin to add a new actionmenu titled Open Folder."*


* **Applying the project layout:** Click **Apply to Editor/Project**. A popup window will prompt you for a project name. Type `NavPlugin` and press enter.
* **The Result:** The plugin creates a custom ICN layout containing a root `build.xml` Ant runner file, a `META-INF/MANIFEST.MF` metadata directory, main Java configuration action extensions, and deeply nested Dojo WebContent module asset scripts (`DossierPlugin.js`, `ConfigurationPane.html`, etc.).

### 9. Generate Complete Project Structure

* **What it does:** Scaffolds a complete standalone project topology from scratch based on structural instructions (Standard Java, Maven, Spring Boot, Spring MVC architectures).
* **Sample Prompt:**
> *"Create an Employee management Spring Boot microservice layout with clean layered domain port infrastructure configurations."*


* **Applying the project layout:** Click **Apply to Editor/Project**, type your project folder name into the input popup dialog container (e.g., `EmployeeManagement`), and hit OK. The system builds the entire nested directory path tree, generates the `pom.xml`, handles the classpath entries safely without nesting collision errors, and updates your Package Explorer view automatically.

---

## 5. Troubleshooting & Pro-Tips

* **Solving Compilation Errors on Generated Projects:** When creating an enterprise architecture layout (like the *IBM Navigator Plugin*), the AI scaffolds all required source files, but you must manually right-click the project, go to **Build Path -> Configure Build Path**, and link your local runtime library dependencies (like `navigatorAPI.jar` or Spring dependencies) to resolve compiler import warnings.
* **Avoiding Destructive Multi-File Overwrites:** The `Implement Feature` mode replaces entire files on disk to prevent corrupted partial inserts. Always make sure your files are saved or version-controlled via Git before executing bulk project implementations.
* **Handling AI Truncation Issues:** If a massive project generation response cuts off mid-way due to LLM maximum output limit scopes, simply type *"continue"* into the **Chat (Freeform)** field to receive the remaining file segments seamlessly.