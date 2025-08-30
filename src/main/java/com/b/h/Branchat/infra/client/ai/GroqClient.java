package com.b.h.Branchat.infra.client.ai;

import com.b.h.Branchat.domain.summarize.exception.SummarizeErrorCode;
import com.b.h.Branchat.domain.summarize.exception.SummarizeException;
import com.b.h.Branchat.domain.summarize.port.AiClientPort;
import com.b.h.Branchat.infra.client.ai.dto.request.GroqChatMessage;
import com.b.h.Branchat.infra.client.ai.dto.request.GroqChatRequest;
import com.b.h.Branchat.infra.client.ai.dto.response.GroqChatResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Primary
@Component
public class GroqClient implements AiClientPort {

    private final RestClient restClient;

    @Value("${groq.api.key}")
    private String groqApiKey;

    public GroqClient(@Qualifier("groqRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public String freeAiSummarization(String prompt) {
        List<GroqChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new GroqChatMessage("system", AiClientPort.SYSTEM_PROMPT_FOR_SUMMARY));
        chatMessages.add(new GroqChatMessage("user", prompt));

        GroqChatRequest request = new GroqChatRequest(
            chatMessages,
            "llama-3.1-8b-instant",
            0.2,
            128,
            1.0,
            false,
            null
        );

        GroqChatResponse response = restClient.post()
            .uri("/chat/completions")
            .header("Authorization", "Bearer " + groqApiKey)
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .onStatus(org.springframework.http.HttpStatusCode::is4xxClientError, (req, res) -> {
                throw new RuntimeException("Groq 4xx: " + res.getStatusCode());
            })
            .onStatus(org.springframework.http.HttpStatusCode::is5xxServerError, (req, res) -> {
                throw new RuntimeException("Groq 5xx: " + res.getStatusCode());
            })
            .body(GroqChatResponse.class);

        return Optional.ofNullable(response)
            .map(GroqChatResponse::choices)
            .filter(choices -> !choices.isEmpty())
            .map(choices -> choices.get(0).message().content())
            .orElseThrow(() -> new SummarizeException(SummarizeErrorCode.GROQ_RESPONSE_ERROR));
    }
}
