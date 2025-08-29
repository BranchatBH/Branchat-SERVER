package com.b.h.Branchat.domain.summarize.service;

import com.b.h.Branchat.domain.node.dto.request.MessageContent;
import com.b.h.Branchat.domain.node.enums.MessageRole;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SummarizeService {

    private static final int SHORT_CONVERSATION_THRESHOLD = 5;

    /**
     * Returns a summary of the conversation as a single string.
     */
    public String freeTierSummarize(List<MessageContent> messages) {
        List<MessageContent> summaryMessages = getSummaryMessages(messages);

    }

    /**
     * Performs the core logic of selecting the relevant messages for a summary.
     * @return A list of MessageContent objects for the summary.
     */
    public List<MessageContent> getSummaryMessages(List<MessageContent> messages) {
        if (messages == null || messages.isEmpty()) {
            return Collections.emptyList();
        }

        if (messages.size() <= SHORT_CONVERSATION_THRESHOLD) {
            return new ArrayList<>(messages); // Return a mutable copy of the full list
        }

        return getSelectiveSummaryMessages(messages);
    }

    //첫번째 프롬프트, 마지막 두쌍 합쳐서 리스트로 한번에 저장함.
    private List<MessageContent> getSelectiveSummaryMessages(List<MessageContent> messages) {
        List<MessageContent> summaryMessages = new ArrayList<>();

        addFirstUserMessage(messages, summaryMessages);

        List<MessageContent> lastPairs = findLastTwoCompletePairs(messages);
        addMessagesToSummary(lastPairs, summaryMessages);

        return summaryMessages;
    }

    private void addFirstUserMessage(List<MessageContent> source, List<MessageContent> destination) {
        source.stream()
            .filter(m -> m.role() == MessageRole.USER)
            .findFirst()
            .ifPresent(destination::add);
    }

    private List<MessageContent> findLastTwoCompletePairs(List<MessageContent> messages) {
        List<MessageContent> foundPairs = new ArrayList<>();
        int pairsToFind = 2;

        for (int i = messages.size() - 1; i > 0 && pairsToFind > 0; i--) {
            MessageContent currentMsg = messages.get(i);
            MessageContent previousMsg = messages.get(i - 1);

            if (currentMsg.role() == MessageRole.AI && previousMsg.role() == MessageRole.USER) {
                foundPairs.add(currentMsg);
                foundPairs.add(previousMsg);
                pairsToFind--;
                i--; // Skip the 'user' message we just processed
            }
        }

        Collections.reverse(foundPairs); // Restore chronological order
        return foundPairs;
    }

    private void addMessagesToSummary(List<MessageContent> source, List<MessageContent> destination) {
        for (MessageContent message : source) {
            if (!destination.contains(message)) {
                destination.add(message);
            }
        }
    }
}
