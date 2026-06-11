package com.copilot.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.dialogs.InputDialog;

import com.copilot.Activator;
import com.copilot.preferences.PreferenceConstants;
import com.copilot.prompts.PromptBuilder;
import com.copilot.network.AIClient;
import com.copilot.project.ProjectGenerator;

public class CopilotView extends ViewPart {

    private Browser browser;
    private Text inputTextBox;
    private Combo operationsCombo;
    private Button sendBtn;
    private Button applyEditorBtn;
    
    private String lastExtractedCodeBlock = "";
    private String lastFullRawAiResponse = "";
    private String lastExecutedOperation = ""; 
    
    // Upgraded CSS for beautiful, dark-themed code blocks
    private final String HTML_HEADER = "<html><head><style>" +
        "body { font-family: 'Segoe UI', Tahoma, Arial, sans-serif; font-size: 13px; background-color: #f0f4f9; margin: 0; padding: 15px; color: #202124; line-height: 1.6; }" +
        ".msg-container { width: 100%; clear: both; overflow: hidden; margin-bottom: 15px; }" +
        ".msg-user { background-color: #d3e3fd; border-radius: 18px 18px 0 18px; padding: 12px 18px; float: right; max-width: 85%; color: #041e49; box-shadow: 0 1px 2px rgba(0,0,0,0.05); }" +
        ".msg-ai { background-color: #ffffff; border-radius: 18px 18px 18px 0; padding: 16px; float: left; max-width: 90%; color: #1f1f1f; box-shadow: 0 2px 4px rgba(0,0,0,0.08); border: 1px solid #e8eaed; white-space: pre-wrap; }" +
        ".msg-ai pre { background-color: #1e1e1e; color: #d4d4d4; padding: 14px; border-radius: 8px; overflow-x: auto; font-family: Consolas, 'Courier New', monospace; font-size: 12px; margin: 12px 0; border: 1px solid #000; box-shadow: inset 0 1px 3px rgba(0,0,0,0.3); white-space: pre; }" +
        ".file-block { background-color: #1e1e1e; border-radius: 6px; margin: 16px 0; overflow: hidden; border: 1px solid #2d2d2d; box-shadow: 0 4px 6px rgba(0,0,0,0.2); }" +
        ".file-header { background-color: #2d2d2d; color: #cccccc; padding: 8px 14px; font-family: 'Segoe UI', sans-serif; font-size: 12px; border-bottom: 1px solid #111; font-weight: bold; display: flex; align-items: center; }" +
        ".file-block pre { background-color: transparent; margin: 0; border: none; border-radius: 0; box-shadow: none; padding: 14px; color: #d4d4d4; font-family: Consolas, monospace; font-size: 12px; white-space: pre; overflow-x: auto; }" +
        ".msg-sys { color: #5f6368; font-size: 11px; text-align: center; margin-bottom: 20px; font-weight: bold; text-transform: uppercase; letter-spacing: 1px; }" +
        ".inline-code { background-color: #f1f3f4; color: #b31412; padding: 2px 5px; border-radius: 4px; font-family: Consolas, monospace; font-size: 12px; }" +
        ".thinking { color: #888; font-style: italic; animation: pulse 1.5s infinite; }" +
        "</style></head><body>";
        
    private StringBuilder conversationTimeline = new StringBuilder(HTML_HEADER + "<div class='msg-sys'>— AI Workspace Initialized —</div>");

    public void createPartControl(Composite parent) {
        parent.setLayout(new GridLayout(1, false));

        // 1. Browser Window (Top)
        browser = new Browser(parent, SWT.BORDER);
        browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        browser.setText(conversationTimeline.toString() + "</body></html>");

        // 2. Input & Controls Panel (Bottom)
        Composite bottomBar = new Composite(parent, SWT.NONE);
        bottomBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        bottomBar.setLayout(new GridLayout(1, false)); 

        // 2a. Dropdown moved above text box
        operationsCombo = new Combo(bottomBar, SWT.READ_ONLY);
        operationsCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        operationsCombo.add(PromptBuilder.OP_CHAT);
        operationsCombo.add(PromptBuilder.OP_EXPLAIN);
        operationsCombo.add(PromptBuilder.OP_SUMMARIZE);
        operationsCombo.add(PromptBuilder.OP_FIX_BUG);
        operationsCombo.add(PromptBuilder.OP_ADD_CODE);
        operationsCombo.add(PromptBuilder.OP_GENERATE_CODE);
        operationsCombo.add(PromptBuilder.OP_IMPLEMENT_FEATURE);
        operationsCombo.add(PromptBuilder.OP_CREATE_NAV_PLUGIN); 
        operationsCombo.add(PromptBuilder.OP_GENERATE_PROJECT);
        operationsCombo.select(0); 

        // 2b. Text Input
        inputTextBox = new Text(bottomBar, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
        GridData textData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        textData.heightHint = 55;
        inputTextBox.setLayoutData(textData);

        // 2c. Buttons container (Right-aligned)
        Composite buttonBar = new Composite(bottomBar, SWT.NONE);
        GridLayout btnLayout = new GridLayout(2, false);
        btnLayout.marginWidth = 0;
        btnLayout.marginHeight = 0;
        buttonBar.setLayout(btnLayout);
        buttonBar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));

        sendBtn = new Button(buttonBar, SWT.PUSH);
        sendBtn.setText("Send");
        
        applyEditorBtn = new Button(buttonBar, SWT.PUSH);
        applyEditorBtn.setText("Apply to Editor/Project");
        applyEditorBtn.setEnabled(false);

        // 2d. AI Disclaimer Label (Centered at the very bottom)
        Label disclaimerLabel = new Label(bottomBar, SWT.CENTER);
        disclaimerLabel.setText("Response generated by AI and can make mistakes.");
        disclaimerLabel.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
        GridData disclaimerData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        disclaimerData.verticalIndent = 5; // Adds a tiny bit of breathing room above the text
        disclaimerLabel.setLayoutData(disclaimerData);
        
        Label disclaimerLabel1 = new Label(bottomBar, SWT.CENTER);
        disclaimerLabel1.setText("Developed by Akash Kumar");
        disclaimerLabel1.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
        GridData disclaimerData1 = new GridData(SWT.CENTER, SWT.CENTER, true, false);
//        disclaimerData1.verticalIndent = 5; // Adds a tiny bit of breathing room above the text
        disclaimerLabel1.setLayoutData(disclaimerData1);

        // Event Listeners
        sendBtn.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                executeAiInferenceTransaction();
            }
        });

        applyEditorBtn.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handleSmartApplyAction();
            }
        });
    }

    private EditorContext fetchEditorContext() {
        EditorContext context = new EditorContext();
        try {
            IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            if (editor == null) return context;

            if (editor.getEditorInput() instanceof IFileEditorInput) {
                IFile file = ((IFileEditorInput) editor.getEditorInput()).getFile();
                context.setProjectName(file.getProject().getName());
                context.setFileName(file.getName());
                context.setFileType(file.getFileExtension());
                context.setContainerPath(file.getParent().getProjectRelativePath().toString());
            }

            if (editor instanceof ITextEditor) {
                ITextEditor textEditor = (ITextEditor) editor;
                
                org.eclipse.jface.text.IDocument doc = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
                if (doc != null) context.setFullFileContent(doc.get());

                ISelection selection = textEditor.getSelectionProvider().getSelection();
                if (selection instanceof ITextSelection) {
                    context.setSelectedText(((ITextSelection) selection).getText());
                }
            }
        } catch (Exception ex) { }
        return context;
    }

    private void executeAiInferenceTransaction() {
        final String rawUserPrompt = inputTextBox.getText();
        if (rawUserPrompt.trim().isEmpty()) return;
        
        final String selectedOperation = operationsCombo.getText();
        final EditorContext currentContext = fetchEditorContext();
        lastExecutedOperation = selectedOperation;
        
        sendBtn.setEnabled(false);
        applyEditorBtn.setEnabled(false);
        inputTextBox.setEnabled(false);
        
        String escapedPrompt = rawUserPrompt.replace("<", "&lt;").replace(">", "&gt;");
        appendWebHistory("<div class='msg-container'><div class='msg-user'><b>You:</b><br/>" + escapedPrompt + " <br/><span style='font-size:10px; opacity:0.7;'>[" + selectedOperation + "]</span></div></div>");
        inputTextBox.setText("");

        String thinkingState = conversationTimeline.toString() + "<div class='msg-container'><div class='msg-ai'><span class='thinking'>✦ Thinking...</span></div></div><script>window.scrollTo(0, document.body.scrollHeight);</script></body></html>";
        browser.setText(thinkingState);

        Job callAgentJob = new Job("Contacting AI Provider Engine...") {
            protected IStatus run(IProgressMonitor monitor) {
                IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
                String url = preferenceStore.getString(PreferenceConstants.P_ENDPOINT);
                String key = preferenceStore.getString(PreferenceConstants.P_API_KEY);
                String model = preferenceStore.getString(PreferenceConstants.P_MODEL);

                String formattedPrompt = PromptBuilder.build(selectedOperation, rawUserPrompt, currentContext);

                try {
                    final String rawAiResponse = AIClient.postRequest(url, key, model, formattedPrompt);
                    lastFullRawAiResponse = rawAiResponse; 
                    extractCodeBlocks(rawAiResponse);
                    
                    Display.getDefault().asyncExec(new Runnable() {
                        public void run() {
                            String styledResponse = rawAiResponse.replace("<", "&lt;").replace(">", "&gt;");
                            
                            // FIXED REGEX: Matches the unescaped quotes exactly how the String formatter left them
                            styledResponse = styledResponse.replaceAll(
                                "&lt;file\\s+path=['\"](.*?)['\"]&gt;([\\s\\S]*?)&lt;/file&gt;", 
                                "</p><div class='file-block'><div class='file-header'>📄 $1</div><pre>$2</pre></div><p>"
                            );
                            
                            // Standard Markdown Code Blocks
                            styledResponse = styledResponse.replaceAll("```[a-zA-Z]*\\s*([\\s\\S]*?)```", "</p><pre>$1</pre><p>");
                            
                            // Inline Text formatting
                            styledResponse = styledResponse.replaceAll("\\*\\*(.*?)\\*\\*", "<b>$1</b>");
                            styledResponse = styledResponse.replaceAll("`([^`]*)`", "<span class='inline-code'>$1</span>");
                            
                            appendWebHistory("<div class='msg-container'><div class='msg-ai'><p style='margin-top:0;'><b>✧ Co-pilot AI:</b></p><p>" + styledResponse + "</p></div></div>");
                            restoreUI();
                        }
                    });
                } catch (final Exception exception) {
                    Display.getDefault().asyncExec(new Runnable() {
                        public void run() {
                            appendWebHistory("<div class='msg-container'><div class='msg-ai' style='border:1px solid red; color:red;'><b>Connection Error:</b> " + exception.getMessage() + "</div></div>");
                            restoreUI();
                        }
                    });
                }
                return Status.OK_STATUS;
            }
        };
        callAgentJob.schedule();
    }

    private void restoreUI() {
        sendBtn.setEnabled(true);
        applyEditorBtn.setEnabled(true);
        inputTextBox.setEnabled(true);
        inputTextBox.setFocus();
    }

    private void handleSmartApplyAction() {
        applyEditorBtn.setEnabled(false);

        if (PromptBuilder.OP_GENERATE_PROJECT.equals(lastExecutedOperation) || PromptBuilder.OP_CREATE_NAV_PLUGIN.equals(lastExecutedOperation)) {
            InputDialog dialog = new InputDialog(getSite().getShell(), "Create New Project", "Enter a name for the new generated project:", "NewWorkspaceProject", null);
            if (dialog.open() == org.eclipse.jface.window.Window.OK) {
                scheduleFileWriteJob(dialog.getValue());
            } else {
                applyEditorBtn.setEnabled(true);
            }
        } else if (PromptBuilder.OP_IMPLEMENT_FEATURE.equals(lastExecutedOperation) || lastFullRawAiResponse.contains("&lt;file path=&quot;") || lastFullRawAiResponse.contains("<file path=\"")) {
            EditorContext context = fetchEditorContext();
            String targetProjectName = context.getProjectName();
            if (targetProjectName != null) {
                scheduleFileWriteJob(targetProjectName);
            } else {
                InputDialog dialog = new InputDialog(getSite().getShell(), "Target Project", "No active file found. Enter existing project name:", "MyProject", null);
                if (dialog.open() == org.eclipse.jface.window.Window.OK) {
                    scheduleFileWriteJob(dialog.getValue());
                } else {
                    applyEditorBtn.setEnabled(true);
                }
            }
        } else {
            applyResponseCodeToActiveEditor();
            applyEditorBtn.setEnabled(true); 
        }
    }

    private void scheduleFileWriteJob(final String projectName) {
        Job buildJob = new Job("Writing generated files to workspace...") {
            protected IStatus run(IProgressMonitor monitor) {
                try {
                    ProjectGenerator.buildFromAiResponse(projectName, lastFullRawAiResponse, monitor);
                    Display.getDefault().asyncExec(new Runnable() {
                        public void run() {
                            appendWebHistory("<div class='msg-sys'>✓ Files successfully written to project: " + projectName + "</div>");
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Display.getDefault().asyncExec(new Runnable() {
                        public void run() {
                            applyEditorBtn.setEnabled(true);
                        }
                    });
                }
                return Status.OK_STATUS;
            }
        };
        buildJob.schedule();
    }

    private void extractCodeBlocks(String output) {
        if (output.contains("## FINAL_CODE")) {
            try {
                String target = output.substring(output.indexOf("## FINAL_CODE"));
                String[] segments = target.split("```");
                if (segments.length > 1) {
                    lastExtractedCodeBlock = segments[1].trim();
                    if(lastExtractedCodeBlock.startsWith("java")) lastExtractedCodeBlock = lastExtractedCodeBlock.substring(4).trim();
                    return; 
                }
            } catch (Exception e) { }
        }
        
        if (output.contains("```")) {
            try {
                String[] segments = output.split("```");
                if (segments.length > 1) {
                    lastExtractedCodeBlock = segments[1].trim();
                    if(lastExtractedCodeBlock.startsWith("java")) lastExtractedCodeBlock = lastExtractedCodeBlock.substring(4).trim();
                    else if (lastExtractedCodeBlock.startsWith("xml")) lastExtractedCodeBlock = lastExtractedCodeBlock.substring(3).trim();
                }
            } catch (Exception e) {
                lastExtractedCodeBlock = output;
            }
        } else {
            lastExtractedCodeBlock = output;
        }
    }

    private void applyResponseCodeToActiveEditor() {
        if (lastExtractedCodeBlock.isEmpty()) {
            MessageBox dialog = new MessageBox(getSite().getShell(), SWT.ICON_WARNING | SWT.OK);
            dialog.setMessage("No code snippet found to apply.");
            dialog.open();
            return;
        }

        try {
            IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            if (editor instanceof ITextEditor) {
                ITextEditor textEditor = (ITextEditor) editor;
                org.eclipse.jface.text.IDocument doc = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
                ITextSelection selection = (ITextSelection) textEditor.getSelectionProvider().getSelection();
                doc.replace(selection.getOffset(), selection.getLength(), lastExtractedCodeBlock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appendWebHistory(String htmlSnippet) {
        conversationTimeline.append(htmlSnippet);
        browser.setText(conversationTimeline.toString() + "<script>window.scrollTo(0, document.body.scrollHeight);</script></body></html>");
    }

    public void setFocus() {
        inputTextBox.setFocus();
    }
}