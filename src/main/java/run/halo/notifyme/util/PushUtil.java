package run.halo.notifyme.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import run.halo.notifyme.domain.NotifyMe;
import run.halo.notifyme.domain.PushDo;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@Component
@Slf4j
public class PushUtil {
    private final WebClient webClient = WebClient.create("https://api.anpush.com");

    public void sendFormData(String url, MultiValueMap<String, String> formData) {
        webClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(formData))
            .retrieve()
            .bodyToMono(String.class)
            .doOnSuccess(response -> log.info("AnPush Response: " + response))
            .doOnError(error -> log.error("AnPush Error: " + error.getMessage()))
            .subscribe();
    }


    public void sendRequest(PushDo pushDo, NotifyMe setting) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("title", pushDo.getTitle());
        formData.add("content", pushDo.getContent());
        formData.add("channel", setting.getChannel());
        String url = "/push/" + setting.getApiKey();
        sendFormData(url, formData);
    }
}
