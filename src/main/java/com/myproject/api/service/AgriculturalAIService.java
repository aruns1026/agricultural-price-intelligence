package com.myproject.api.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AgriculturalAIService {

    private final ChatLanguageModel chatModel;

    @Value("${agriculture.crops}")
    private String[] crops;

    @Value("${agriculture.regions}")
    private String[] regions;

    public AgriculturalAIService(@Value("${agriculture.llm.type}") String llmType,
                               @Value("${agriculture.llm.ollama.base-url}") String ollamaBaseUrl,
                               @Value("${agriculture.llm.ollama.model-name}") String ollamaModel,
                               @Value("${agriculture.llm.openai.api-key}") String openAiKey,
                               @Value("${agriculture.llm.openai.model-name}") String openAiModel) {

        if ("openai".equalsIgnoreCase(llmType)) {
            this.chatModel = OpenAiChatModel.builder()
                    .apiKey(openAiKey)
                    .modelName(openAiModel)
                    .build();
            log.info("Initialized OpenAI chat model");
        } else {
            this.chatModel = OllamaChatModel.builder()
                    .baseUrl(ollamaBaseUrl)
                    .modelName(ollamaModel)
                    .build();
            log.info("Initialized Ollama chat model");
        }
    }

    public String getAgriculturalAdvice(String crop, String region, String question) {
        String systemPrompt = buildSystemPrompt(crop, region);

        String response = chatModel.generate(systemPrompt + "\n\nUser Question: " + question);
        log.info("Generated agricultural advice for crop: {}, region: {}", crop, region);

        return response;
    }

    public String getImportImpactAnalysis(String crop, String impactLevel) {
        String prompt = String.format(
            "Analyze the impact of US agricultural imports on Indian %s market with impact level: %s. " +
            "Consider price trends, farmer welfare, and market dynamics. Provide actionable insights.",
            crop, impactLevel
        );

        return chatModel.generate(prompt);
    }

    public String analyzeMarketTrend(String crop, String region) {
        String prompt = String.format(
            "As an agricultural expert, analyze the market trend for %s in %s region. " +
            "Consider seasonal patterns, import pressures, and price forecasts. " +
            "Provide recommendations for farmers.",
            crop, region
        );

        return chatModel.generate(prompt);
    }

    private String buildSystemPrompt(String crop, String region) {
        return String.format(
            "You are an agricultural expert specializing in Indian farming. " +
            "You provide advice on %s cultivation in %s region. " +
            "Consider local climate, soil conditions, market prices, and recent US import developments. " +
            "Always provide practical, actionable advice in simple language suitable for farmers.",
            crop, region
        );
    }
}

