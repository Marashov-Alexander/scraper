package study.polytech.scraper.scrap;

import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import study.polytech.scraper.ScrapRequest;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ScriptsManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptsManager.class);

    private static final String HIDE_MEDIA_SCRIPT = """
            document.querySelectorAll('img').forEach(img => {
                img.style.display = 'none';
                img.style.border = '1px solid #ccc';
            });
            document.querySelectorAll('*').forEach(el => {
                const computedStyle = window.getComputedStyle(el);
                if (computedStyle.backgroundImage !== 'none') {
                    el.style.backgroundImage = 'none';
                    el.style.border = '1px solid #ccc';
                }
            });
            document.querySelectorAll('video, iframe').forEach(el => {
                if (el.tagName.toLowerCase() === 'iframe' &&
                    (el.src.includes("youtube.com") || el.src.includes("vimeo.com"))) {
                    el.style.display = 'none';
                }
                if (el.tagName.toLowerCase() === 'video') {
                    el.style.display = 'none';
                }
            });""";
    private static final String GET_JS_VARIABLES_SCRIPT = """
            var variables = ""
            for (var name in this)
                variables += name + "\\n";
            return variables;""";

    public ScriptsManager() {

    }

    public boolean hideMedia(@NonNull ScrapRequest request, @NonNull ChromeDriver driver) {
        try {
            driver.executeScript(HIDE_MEDIA_SCRIPT);
            return true;
        } catch (RuntimeException e) {
            LOGGER.error("Hide media content error for request [{}]", request, e);
            return false;
        }
    }

    @Nullable
    public String getJsVariables(@NonNull ScrapRequest request, @NonNull ChromeDriver driver) {
        try {
            Object jsVariablesObj = driver.executeScript(GET_JS_VARIABLES_SCRIPT);
            return castToStringSet(jsVariablesObj);
        } catch (RuntimeException e) {
            LOGGER.error("Get js variables error for request [{}]", request, e);
            return null;
        }
    }

    @Nullable
    private String castToStringSet(@Nullable Object jsVariablesObj) {
        if (jsVariablesObj instanceof String) {
            return (String) jsVariablesObj;
        }
        return null;
    }
}
