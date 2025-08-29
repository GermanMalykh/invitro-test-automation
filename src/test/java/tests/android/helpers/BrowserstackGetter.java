package tests.android.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import helpers.Attach;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import tests.android.config.ConfigReader;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public class BrowserstackGetter {

    private static final String TEMPLATE_PATH = "tpl/browserstack_report.ftl";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ExtractableResponse<Response> getSessionInfo(String sessionId) {
        String url = format("https://api.browserstack.com/app-automate/sessions/%s.json", sessionId);

        return RestAssured
                .given()
                .auth().basic(ConfigReader.get("browserstack.user"),
                        ConfigReader.get("browserstack.key"))
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .extract();
    }

    /**
     * Генерирует HTML отчет используя FreeMarker шаблон
     */
    public static String generateHtmlReport(String sessionData) throws IOException, TemplateException {
        // Парсим JSON данные
        JsonNode jsonData = objectMapper.readTree(sessionData);
        JsonNode automationSessionNode = jsonData.path("automation_session");

        // Подготавливаем данные для шаблона
        Map<String, Object> automationSession =
                objectMapper.convertValue(automationSessionNode,
                        new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("automation_session", automationSession);

        // Настраиваем FreeMarker
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassLoaderForTemplateLoading(Attach.class.getClassLoader(), "");
        cfg.setDefaultEncoding("UTF-8");

        // Загружаем и обрабатываем шаблон
        Template template = cfg.getTemplate(TEMPLATE_PATH);

        try (StringWriter writer = new StringWriter()) {
            template.process(templateData, writer);
            return writer.toString();
        }
    }

    public static String videoUrl(String sessionId) {
        return getSessionInfo(sessionId)
                .path("automation_session.video_url");
    }
}
