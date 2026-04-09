package integration.chat;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationParticipantEntity;
import com.dndplatform.test.entity.TestEntityProvider;

import java.time.LocalDateTime;
import java.util.List;

public class ConversationEntityProvider implements TestEntityProvider {

    public static final String NAME = "Test Conversation";
    public static final String TYPE = "GROUP";
    public static final Long CREATOR_USER_ID = 1L;
    public static final Long PARTICIPANT_USER_ID = 2L;

    @Override
    public List<Object> provideEntities() {
        var conversation = new ConversationEntity();
        conversation.name = NAME;
        conversation.type = TYPE;
        conversation.createdBy = CREATOR_USER_ID;
        conversation.createdAt = LocalDateTime.now();
        conversation.updatedAt = LocalDateTime.now();

        var creator = new ConversationParticipantEntity();
        creator.conversation = conversation;
        creator.userId = CREATOR_USER_ID;
        creator.role = "ADMIN";

        var participant = new ConversationParticipantEntity();
        participant.conversation = conversation;
        participant.userId = PARTICIPANT_USER_ID;
        participant.role = "MEMBER";

        return List.of(conversation, creator, participant);
    }
}
