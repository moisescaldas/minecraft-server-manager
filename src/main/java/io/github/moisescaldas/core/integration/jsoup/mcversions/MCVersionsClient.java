package io.github.moisescaldas.core.integration.jsoup.mcversions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import io.github.moisescaldas.core.exceptions.ApplicationServerException;
import io.github.moisescaldas.core.exceptions.BusinessRuleException;
import io.github.moisescaldas.core.util.MessagesPropertiesUtil;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MCVersionsClient {

    @Resource(lookup = "url/mcversions", type = URL.class)
    private URL endpoint;

    public String getUrlDonwloadString(String minecraftVersion) {
        try {
            var doc = Jsoup.connect(endpoint.toURI() + "/" + minecraftVersion).get();
            var element = doc.selectXpath("/html/body/main/div/div[1]/div[2]/div[1]/a");
            return element.attr("href");
        } catch (HttpStatusException ex) {
            throw new BusinessRuleException(MessagesPropertiesUtil.geString("E0001", minecraftVersion), ex);
        } catch (IOException | URISyntaxException ex) {
            throw new ApplicationServerException(ex);
        }
    }
}
