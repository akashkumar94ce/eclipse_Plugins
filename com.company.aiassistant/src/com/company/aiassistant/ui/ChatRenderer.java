package com.company.aiassistant.ui;

import java.util.List;

import com.company.aiassistant.ai.ResponseFormatter;
import com.company.aiassistant.model.ChatMessage;
import com.company.aiassistant.model.MessageType;

public final class ChatRenderer {

    private ChatRenderer() {
    }

    /**
     * Generates complete HTML page.
     */
    public static String render(
            List<ChatMessage> messages) {

        StringBuilder html =
                new StringBuilder();

        html.append("<html>");
        html.append("<head>");

        html.append(buildCss());

        html.append("</head>");
        html.append("<body>");

        if (messages != null) {

            for (ChatMessage message
                    : messages) {

                html.append(
                        renderMessage(
                                message));
            }
        }

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    /**
     * Renders one message.
     */
    private static String renderMessage(
            ChatMessage message) {

        StringBuilder html =
                new StringBuilder();

        MessageType type =
                message.getType();

        String cssClass;

        String title;

        if (type == MessageType.USER) {

            cssClass = "user-message";
            title = "User";

        } else if (type == MessageType.ERROR) {

            cssClass = "error-message";
            title = "Error";

        } else {

            cssClass = "ai-message";
            title = "AI Response";
        }

        html.append(
                "<div class='")
            .append(cssClass)
            .append("'>");

        html.append(
                "<div class='message-title'>")
            .append(title)
            .append("</div>");

       /* html.append(
                ResponseFormatter.toHtml(
                        message.getContent()));*/
        
        if (type == MessageType.AI) {

            /*
             * Already converted to HTML by
             * MarkdownRenderer.
             */
            html.append(
                    message.getContent());

        } else {

            /*
             * User and error messages.
             */
            html.append(
                    ResponseFormatter.toHtml(
                            message.getContent()));
        }

        html.append("</div>");

        return html.toString();
    }

    /**
     * Embedded CSS.
     */
    private static String buildCss() {

        StringBuilder css =
                new StringBuilder();

        css.append("<style>");

        css.append(".code-block {");
        css.append("background:#f7f7f7;");
        css.append("border:1px solid #d0d0d0;");
        css.append("padding:12px;");
        css.append("margin:10px 0;");
        css.append("overflow:auto;");
        css.append("font-family: Consolas, Courier New;");
        css.append("font-size:12px;");
        css.append("}");

        css.append(".user-message {");
        css.append("border:1px solid #cccccc;");
        css.append("padding:10px;");
        css.append("margin-bottom:15px;");
        css.append("background:#f5f5f5;");
        css.append("}");

        css.append(".ai-message {");
        css.append("border:1px solid #dcdcdc;");
        css.append("padding:12px;");
        css.append("margin-bottom:15px;");
        css.append("background:#ffffff;");
        css.append("border-radius:4px;");
        css.append("}");

        css.append(".error-message {");
        css.append("border:1px solid #ff8080;");
        css.append("padding:10px;");
        css.append("margin-bottom:15px;");
        css.append("background:#fff0f0;");
        css.append("}");

        css.append(".code-block {");
        css.append("background:#f4f4f4;");
        css.append("border:1px solid #cccccc;");
        css.append("padding:10px;");
        css.append("overflow:auto;");
        css.append("font-family: Consolas;");
        css.append("}");

        css.append("h1 {");
        css.append("font-size:20px;");
        css.append("}");

        css.append("h2 {");
        css.append("font-size:16px;");
        css.append("}");

        css.append("p {");
        css.append("margin:4px 0;");
        css.append("}");

        css.append("</style>");

        return css.toString();
    }
}