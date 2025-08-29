package com.b.h.Branchat.infra.client.ai;

import com.b.h.Branchat.domain.node.dto.request.MessageContent;
import com.b.h.Branchat.domain.summarize.port.AiClientPort;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class OpenAiClient implements AiClientPort {
    @Override
    public String freeAiSummarization(List<MessageContent> messages) {

    }
}
