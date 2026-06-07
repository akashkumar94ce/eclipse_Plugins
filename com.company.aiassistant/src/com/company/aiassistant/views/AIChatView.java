package com.company.aiassistant.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.company.aiassistant.ai.AIActionType;
import com.company.aiassistant.ai.AIRequest;
import com.company.aiassistant.ai.AIResponse;
import com.company.aiassistant.ai.AIServiceImpl;
import com.company.aiassistant.model.ChatMessage;
import com.company.aiassistant.model.MessageType;
import com.company.aiassistant.project.GeneratedFile;
import com.company.aiassistant.project.ProjectGenerationParser;
import com.company.aiassistant.project.ProjectGenerationResult;
import com.company.aiassistant.ui.ChatRenderer;
import com.company.aiassistant.ui.MarkdownRenderer;
import com.company.aiassistant.util.CodeBlockExtractor;
import com.company.aiassistant.util.EditorUtil;
import com.company.aiassistant.workspace.WorkspaceExecutor;

/**
 * Main AI Chat View.
 *
 * Phase 1:
 * - Chat UI
 * - Ollama Integration
 * - Session-only history
 */
public class AIChatView extends ViewPart {

    public static final String ID =
            "com.company.aiassistant.views.AIChatView";

    /*
     * Conversation history area.
     */
//    private StyledText conversationArea;
    private Browser conversationBrowser;
    private List<ChatMessage> messages =
            new ArrayList<ChatMessage>();

    /*
     * User prompt area.
     */
    private Text promptText;

    /*
     * Buttons.
     */
    private Button sendButton;
    private Button clearButton;
    private Button applyButton;
    private Button executeWorkspaceButton;
    private Button generateProjectButton;

    /*
     * Status label.
     */
    private Label statusLabel;
    
    private AIActionType currentActionType =
            AIActionType.CHAT;
    
    /**
     * Last raw AI response.
     */
    private String lastRawAIResponse = "";


    public AIChatView() {
    }

    @Override
    public void createPartControl(
            Composite parent) {

        /*
         * Main layout.
         */
        GridLayout layout =
                new GridLayout(1, false);

        parent.setLayout(layout);

        createConversationArea(parent);

        createPromptArea(parent);

        createButtonArea(parent);

        createStatusArea(parent);
    }

    /**
     * Creates chat history area.
     */
    private void createConversationArea(
            Composite parent) {

        try {

            conversationBrowser =
                    new Browser(
                            parent,
                            SWT.NONE);

            GridData gd =
                    new GridData(
                            SWT.FILL,
                            SWT.FILL,
                            true,
                            true);

            gd.heightHint = 350;

            conversationBrowser
                    .setLayoutData(gd);

            refreshConversation();

        } catch (SWTError e) {

            e.printStackTrace();

            throw e;
        }
    }

    /**
     * Creates prompt input area.
     */
    private void createPromptArea(
            Composite parent) {

        promptText =
                new Text(
                        parent,
                        SWT.BORDER
                        | SWT.MULTI
                        | SWT.WRAP
                        | SWT.V_SCROLL);

        GridData gd =
                new GridData(
                        SWT.FILL,
                        SWT.FILL,
                        true,
                        false);

        gd.heightHint = 80;

        promptText.setLayoutData(gd);
    }

    /**
     * Creates Send/Clear buttons.
     */
    private void createButtonArea(
            Composite parent) {

        Composite buttonComposite =
                new Composite(parent, SWT.NONE);

        buttonComposite.setLayout(
                new GridLayout(2, false));

        buttonComposite.setLayoutData(
                new GridData(
                        SWT.FILL,
                        SWT.CENTER,
                        true,
                        false));

        sendButton =
                new Button(
                        buttonComposite,
                        SWT.PUSH);

        sendButton.setText("Send");

        clearButton =
                new Button(
                        buttonComposite,
                        SWT.PUSH);

        clearButton.setText("Clear");
        
        applyButton =
                new Button(
                        buttonComposite,
                        SWT.PUSH);

        applyButton.setText(
                "Apply To Editor");
        
        executeWorkspaceButton =
                new Button(
                        buttonComposite,
                        SWT.PUSH);

        executeWorkspaceButton.setText(
                "Execute Workspace Action");

        executeWorkspaceButton.setEnabled(
                false);
        
        generateProjectButton = new Button(
                buttonComposite,
                SWT.PUSH);

        generateProjectButton.setText(
                "Generate Project");

        /*
         * Send prompt.
         */
        sendButton.addSelectionListener(
            new SelectionAdapter() {

                @Override
                public void widgetSelected(
                        SelectionEvent e) {

                    sendPrompt();
                    executeWorkspaceButton.setEnabled(
                            false);
                }
            });

        /*
         * Clear conversation.
         */
        clearButton.addSelectionListener(
            new SelectionAdapter() {

                @Override
                public void widgetSelected(
                        SelectionEvent e) {

//                    conversationArea.setText("");
                	messages.clear();

                	refreshConversation();
                }
            });
        
        applyButton.addSelectionListener(
                new SelectionAdapter() {

                    @Override
                    public void widgetSelected(
                            SelectionEvent e) {

                        applyToEditor();
                    }
                });
        
        executeWorkspaceButton
        .addSelectionListener(
                new SelectionAdapter() {

                    @Override
                    public void widgetSelected(
                            SelectionEvent e) {

                        executeWorkspaceAction();
                    }
                });
        
        generateProjectButton
        .addSelectionListener(
                new SelectionAdapter() {

                    @Override
                    public void widgetSelected(
                            SelectionEvent e) {

                        currentActionType =
                                AIActionType.GENERATE_PROJECT;

                        promptText.setFocus();

                        statusLabel.setText(
                                "Enter project requirements and click Send.");
                    }
                });
    }

    /**
     * Status area.
     */
    private void createStatusArea(
            Composite parent) {

        statusLabel =
                new Label(parent, SWT.NONE);

        statusLabel.setText("Ready");

        statusLabel.setLayoutData(
                new GridData(
                        SWT.FILL,
                        SWT.CENTER,
                        true,
                        false));
    }

    /**
     * Append user message.
     */
    public void appendUserMessage(
            String text) {

        messages.add(
                new ChatMessage(
                        MessageType.USER,
                        text));

        refreshConversation();
    }

    /**
     * Append AI message.
     */
    public void appendAIMessage(
            String text) {

        messages.add(
                new ChatMessage(
                        MessageType.AI,
                        text));

        refreshConversation();
    }

    /**
     * Append error.
     */
    public void appendErrorMessage(
            String text) {

        messages.add(
                new ChatMessage(
                        MessageType.ERROR,
                        text));

        refreshConversation();
    }

    /**
     * Set prompt programmatically.
     */
    public void setPrompt(
            String prompt) {

        promptText.setText(prompt);
    }

    /**
     * Sends prompt to AI.
     */
    public void sendPrompt() {

        final String prompt =
                promptText.getText();

        if (prompt == null
                || prompt.trim().length() == 0) {

            return;
        }

        appendUserMessage(prompt);

        promptText.setText("");

        sendButton.setEnabled(false);

        statusLabel.setText(
                "Thinking...");

        /*
         * Never call AI on UI thread.
         */
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    AIRequest request =
                            new AIRequest();

                    /*request.setActionType(
                            AIActionType.CHAT);*/
                    
                    request.setActionType(
                            currentActionType);

                    request.setPrompt(
                            prompt);
                    
                    /*
                     * For explain/fix/improve actions
                     * PromptBuilder expects selectedCode.
                     */
                    request.setSelectedCode(
                            prompt);
                    
                    System.out.println(
                            "ACTION = "
                            + currentActionType);

                    System.out.println(
                            "PROMPT = "
                            + prompt);

                    final AIResponse response =
                            new AIServiceImpl()
                                    .execute(request);

                    Display.getDefault()
                           .asyncExec(
                        new Runnable() {

                            @Override
                            public void run() {

                                if (response.isSuccess()) {
                                	
                                	lastRawAIResponse =
                                	        response.getResponse();
                                	
                                	if (currentActionType
                                	        == AIActionType.GENERATE_PROJECT) {

                                	    currentActionType =
                                	            AIActionType.CHAT;
                                	}
                                	
                                	boolean workspaceAction =
                                	        isWorkspaceResponse(
                                	                lastRawAIResponse);

                                	executeWorkspaceButton
                                	        .setEnabled(
                                	                workspaceAction);
                                	
                                	String renderedResponse =
                                	        MarkdownRenderer.toHtml(
                                	        		response.getResponse());

                                	appendAIMessage(
                                			renderedResponse);
                                    /*appendAIMessage(
                                            response.getResponse());*/

                                } else {

                                    appendErrorMessage(
                                            response.getErrorMessage());
                                }

                                statusLabel.setText(
                                        "Ready");

                                sendButton.setEnabled(
                                        true);
                            }
                        });

                } catch (final Exception e) {

                    Display.getDefault()
                           .asyncExec(
                        new Runnable() {

                            @Override
                            public void run() {

                                appendErrorMessage(
                                        e.getMessage());

                                statusLabel.setText(
                                        "Error");

                                sendButton.setEnabled(
                                        true);
                            }
                        });
                }
            }

        }).start();
    }

    public void setActionType(
            AIActionType actionType) {

        if (actionType == null) {

            currentActionType =
                    AIActionType.CHAT;

            return;
        }

        currentActionType =
                actionType;
    }
    
    @Override
    public void setFocus() {

        if (promptText != null
                && !promptText.isDisposed()) {

            promptText.setFocus();
        }
    }
    
    /**
     * Rebuild browser content.
     */
    private void refreshConversation() {

        if (conversationBrowser == null
                || conversationBrowser.isDisposed()) {

            return;
        }

        String html =
                ChatRenderer.render(
                        messages);

        conversationBrowser.setText(
                html);
    }
    
    private void applyToEditor() {

        try {

            String code = null;

            /*
             * Preferred extraction:
             * FINAL_CODE
             * IMPROVED_CODE
             * GENERATED_CODE
             */
            code =
                    CodeBlockExtractor
                        .extractPreferredCodeBlock(
                                lastRawAIResponse);

            /*
             * Backward compatibility.
             */
            if (code == null
                    || code.trim().isEmpty()) {

                code =
                        CodeBlockExtractor
                            .extractFirstCodeBlock(
                                    lastRawAIResponse);
            }

            if (code == null
                    || code.trim().isEmpty()) {

                MessageDialog.openInformation(
                        getSite().getShell(),
                        "AI Assistant",
                        "No code block found in AI response.");

                return;
            }

            boolean success;

            if (currentActionType
                    == AIActionType.GENERATE_CODE) {

                success =
                        EditorUtil.insertAtCursor(
                                code);

            } else {

                success =
                        EditorUtil.replaceSelection(
                                code);
            }

            if (success) {

                MessageDialog.openInformation(
                        getSite().getShell(),
                        "AI Assistant",
                        "Code applied successfully.");

            } else {

                MessageDialog.openError(
                        getSite().getShell(),
                        "AI Assistant",
                        "Failed to apply code.");
            }

        } catch (Exception ex) {

            ex.printStackTrace();

            MessageDialog.openError(
                    getSite().getShell(),
                    "AI Assistant",
                    ex.getMessage());
        }
    }
    
    private void applyToEditor1() {

        try {

            String code =
                    CodeBlockExtractor
                        .extractFirstCodeBlock(
                                lastRawAIResponse);

            if (code == null
                    || code.trim()
                           .length() == 0) {

                MessageDialog.openInformation(
                        getSite().getShell(),
                        "AI Assistant",
                        "No code block found in AI response.");

                return;
            }

            boolean success;

            if (currentActionType
                    == AIActionType.GENERATE_CODE) {

                success =
                        EditorUtil.insertAtCursor(
                                code);

            } else {

                success =
                        EditorUtil.replaceSelection(
                                code);
            }

            if (success) {

                MessageDialog.openInformation(
                        getSite().getShell(),
                        "AI Assistant",
                        "Code applied successfully.");

            } else {

                MessageDialog.openError(
                        getSite().getShell(),
                        "AI Assistant",
                        "Failed to apply code.");
            }

        } catch (Exception ex) {

            ex.printStackTrace();

            MessageDialog.openError(
                    getSite().getShell(),
                    "AI Assistant",
                    ex.getMessage());
        }
    }
    
    private boolean isWorkspaceResponse(
            String response) {

        if (response == null) {

            return false;
        }

        return response.contains(
                "PROJECT_ACTION:");
    }
    
    private void executeWorkspaceAction() {

        try {

            if (lastRawAIResponse == null
                    || lastRawAIResponse.trim()
                                        .isEmpty()) {

                MessageDialog.openInformation(
                        getSite().getShell(),
                        "AI Assistant",
                        "No AI response available.");

                return;
            }

            ProjectGenerationResult result =
                    ProjectGenerationParser.parse(
                            lastRawAIResponse);

            String summary =
                    buildExecutionSummary(
                            result);

            boolean proceed =
                    MessageDialog.openQuestion(
                            getSite().getShell(),
                            "Workspace Action",
                            summary
                            + "\n\nContinue?");

            if (!proceed) {

                return;
            }

            WorkspaceExecutor.execute(
                    result);

            MessageDialog.openInformation(
                    getSite().getShell(),
                    "AI Assistant",
                    "Workspace action completed.");

        } catch (Exception e) {

            e.printStackTrace();

            MessageDialog.openError(
                    getSite().getShell(),
                    "AI Assistant",
                    e.getMessage());
        }
    }
    
    private String buildExecutionSummary(
            ProjectGenerationResult result) {

        StringBuilder sb =
                new StringBuilder();

        sb.append(
                "Action: ")
          .append(
                  result.getActionType())
          .append(
                  "\n\n");

        for (GeneratedFile file
                : result.getProject()
                        .getFiles()) {

            sb.append(
                    "- ");

            if (file.getPackageName() != null) {

                sb.append(
                        file.getPackageName())
                  .append(
                          ".");
            }

            sb.append(
                    file.getFileName())
              .append(
                      "\n");
        }

        return sb.toString();
    }
}