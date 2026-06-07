package com.company.aiassistant.util;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

public final class EditorUtil {

    private EditorUtil() {
    }

    /**
     * Returns currently selected text.
     */
    public static String getSelectedText() {

        try {

            IEditorPart editor =
                    getActiveEditor();
            
            if (editor == null) {
                return "";
            }

            if (!(editor instanceof ITextEditor)) {

                return "";
            }

            ITextEditor textEditor =
                    (ITextEditor) editor;

            ITextSelection selection =
                    (ITextSelection)
                    textEditor.getSelectionProvider()
                              .getSelection();

            return selection.getText();

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }
    }

    /**
     * Returns entire editor content.
     */
    public static String getCurrentEditorText() {

        try {

            IEditorPart editor =
                    getActiveEditor();
            
            if (editor == null) {
                return "";
            }

            if (!(editor instanceof ITextEditor)) {

                return "";
            }

            ITextEditor textEditor =
                    (ITextEditor) editor;

            IDocument document =
                    textEditor
                        .getDocumentProvider()
                        .getDocument(
                            textEditor
                                .getEditorInput());

            return document.get();

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }
    }

    /**
     * Returns file name.
     */
    public static String getCurrentFileName() {

        try {

            IEditorPart editor =
                    getActiveEditor();
            
            if (editor == null) {
                return "";
            }

            if (!(editor.getEditorInput()
                    instanceof IFileEditorInput)) {

                return "";
            }

            IFileEditorInput input =
                    (IFileEditorInput)
                    editor.getEditorInput();

            return input.getFile()
                        .getName();

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }
    }

    /**
     * Returns file extension.
     */
    public static String getCurrentFileExtension() {

        try {

            String fileName =
                    getCurrentFileName();

            int idx =
                    fileName.lastIndexOf('.');

            if (idx < 0) {

                return "";
            }

            return fileName.substring(
                    idx + 1);

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }
    }

    /**
     * Returns active editor.
     */
   /* private static IEditorPart getActiveEditor() {

        return PlatformUI
                .getWorkbench()
                .getActiveWorkbenchWindow()
                .getActivePage()
                .getActiveEditor();
    }*/
    
    private static IEditorPart getActiveEditor() {

        try {

            if (PlatformUI.getWorkbench() == null) {
                return null;
            }

            if (PlatformUI.getWorkbench()
                          .getActiveWorkbenchWindow() == null) {
                return null;
            }

            if (PlatformUI.getWorkbench()
                          .getActiveWorkbenchWindow()
                          .getActivePage() == null) {
                return null;
            }

            return PlatformUI.getWorkbench()
                             .getActiveWorkbenchWindow()
                             .getActivePage()
                             .getActiveEditor();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
    
    public static int getCursorOffset() {

        try {

            IEditorPart editor =
                    getActiveEditor();

            if (!(editor instanceof ITextEditor)) {

                return -1;
            }

            ITextEditor textEditor =
                    (ITextEditor) editor;

            ITextSelection selection =
                    (ITextSelection)
                    textEditor.getSelectionProvider()
                              .getSelection();

            return selection.getOffset();

        } catch (Exception e) {

            e.printStackTrace();

            return -1;
        }
    }
    
    public static int getSelectionLength() {

        try {

            IEditorPart editor =
                    getActiveEditor();

            if (!(editor instanceof ITextEditor)) {

                return 0;
            }

            ITextEditor textEditor =
                    (ITextEditor) editor;

            ITextSelection selection =
                    (ITextSelection)
                    textEditor.getSelectionProvider()
                              .getSelection();

            return selection.getLength();

        } catch (Exception e) {

            e.printStackTrace();

            return 0;
        }
    }
    
    public static int getCurrentLineNumber() {

        try {

            IEditorPart editor =
                    getActiveEditor();

            if (!(editor instanceof ITextEditor)) {

                return -1;
            }

            ITextEditor textEditor =
                    (ITextEditor) editor;

            ITextSelection selection =
                    (ITextSelection)
                    textEditor.getSelectionProvider()
                              .getSelection();

            return selection.getStartLine() + 1;

        } catch (Exception e) {

            e.printStackTrace();

            return -1;
        }
    }
    
    public static boolean replaceSelection(
            String newText) {

        try {

            IEditorPart editor =
                    getActiveEditor();

            if (!(editor instanceof ITextEditor)) {

                return false;
            }

            ITextEditor textEditor =
                    (ITextEditor) editor;

            IDocument document =
                    textEditor
                        .getDocumentProvider()
                        .getDocument(
                                textEditor
                                    .getEditorInput());

            ITextSelection selection =
                    (ITextSelection)
                    textEditor.getSelectionProvider()
                              .getSelection();

            document.replace(
                    selection.getOffset(),
                    selection.getLength(),
                    newText);

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
    
    public static boolean insertAtCursor(
            String text) {

        try {

            IEditorPart editor =
                    getActiveEditor();

            if (!(editor instanceof ITextEditor)) {

                return false;
            }

            ITextEditor textEditor =
                    (ITextEditor) editor;

            IDocument document =
                    textEditor
                        .getDocumentProvider()
                        .getDocument(
                                textEditor
                                    .getEditorInput());

            int offset =
                    getCursorOffset();

            if (offset < 0) {

                return false;
            }

            document.replace(
                    offset,
                    0,
                    text);

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
    
    public static boolean replaceEditorContent(
            String text) {

        try {

            IEditorPart editor =
                    getActiveEditor();

            if (!(editor instanceof ITextEditor)) {

                return false;
            }

            ITextEditor textEditor =
                    (ITextEditor) editor;

            IDocument document =
                    textEditor
                        .getDocumentProvider()
                        .getDocument(
                                textEditor
                                    .getEditorInput());

            document.set(text);

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
    
}