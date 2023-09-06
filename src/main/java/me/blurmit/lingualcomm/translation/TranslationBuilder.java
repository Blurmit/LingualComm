package me.blurmit.lingualcomm.translation;

import lombok.NoArgsConstructor;
import me.blurmit.lingualcomm.request.IRequest;

@NoArgsConstructor(staticName = "create")
public class TranslationBuilder implements IRequest {

    private String text;
    private String apiKey;
    private Language language;

    public TranslationBuilder setText(String text) {
        this.text = text;

        return this;
    }

    public String getText() {
        return text;
    }

    public TranslationBuilder setLanguage(Language language) {
        this.language = language;

        return this;
    }

    public Language getLanguage() {
        return language;
    }

    public TranslationBuilder setAPIKey(String apiKey) {
        this.apiKey = apiKey;

        return this;
    }

    public String getAPIKey() {
        return apiKey;
    }

    public String send() {
        return Translation.create(this).send();
    }

}
