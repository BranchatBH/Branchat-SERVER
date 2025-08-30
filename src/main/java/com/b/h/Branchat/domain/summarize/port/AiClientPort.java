package com.b.h.Branchat.domain.summarize.port;

import com.b.h.Branchat.domain.node.dto.request.MessageContent;
import java.util.List;

public interface AiClientPort {

    String SYSTEM_PROMPT_FOR_SUMMARY =
        "You are a helpful assistant who creates a concise summary from conversation snippets. "
            + "Your task is to logically connect the provided snippets—the start of a conversation and the most recent turns—into a single, easy-to-read paragraph. "
            + "Respond in the language of the provided text. "
            + "Produce exactly one summary within 100 characters.";

    String freeAiSummarization(String prompt);


}
