package com.b.h.Branchat.domain.summarize.prompt;

import com.b.h.Branchat.domain.node.dto.request.MessageContent;
import java.util.List;

public class PromptBuilder {
    public static String buildFreeTierPrompt(List<MessageContent> messages){
        StringBuilder prompt = new StringBuilder();
        prompt.append("Please summarize the following conversation snippets:\n\n");

        // 1. 첫 번째 대화 추가
        prompt.append("### First Prompt of the user ###\n");
        prompt.append(messages.get(0).toString()).append("\n\n");

        int messageSize = messages.size();
        if (messageSize > 1) {
            prompt.append("### RECENT CONVERSATION ###\n");
            for (int i = 1; i < messageSize; i++) {
                prompt.append(messages.get(i).toString());
            }
        }

        return prompt.toString();
    }
}
