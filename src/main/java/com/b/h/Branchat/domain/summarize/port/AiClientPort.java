package com.b.h.Branchat.domain.summarize.port;

import com.b.h.Branchat.domain.node.dto.request.MessageContent;
import java.util.List;

public interface AiClientPort {
    String freeAiSummarization(List<MessageContent> messages);


}
