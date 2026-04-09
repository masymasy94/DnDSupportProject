package integration.email;

import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import com.dndplatform.test.entity.TestEntityProvider;

import java.util.List;

public class EmailTemplateEntityProvider implements TestEntityProvider {

    public static final String NAME = "welcome-email";
    public static final String SUBJECT = "Welcome!";
    public static final String HTML_CONTENT = "<html><body>Hello {name}!</body></html>";
    public static final String DESCRIPTION = "Welcome email template";

    @Override
    public List<Object> provideEntities() {
        return List.of(getEntity());
    }

    public static EmailTemplateEntity getEntity() {
        var entity = new EmailTemplateEntity();
        entity.name = NAME;
        entity.subject = SUBJECT;
        entity.htmlContent = HTML_CONTENT;
        entity.description = DESCRIPTION;
        entity.active = true;
        return entity;
    }
}
