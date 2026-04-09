package integration.user;

import com.dndplatform.test.entity.TestEntityProvider;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.domain.util.CryptUtil;

import java.util.List;

public class UserEntityProvider implements TestEntityProvider {

    public static final String USERNAME = "testuser";
    public static final String EMAIL = "testuser@example.com";
    public static final String PASSWORD = "TestPassword123";
    public static final String PASSWORD_HASH = CryptUtil.hashPassword(PASSWORD);
    public static final String ROLE = "PLAYER";

    @Override
    public List<Object> provideEntities() {
        return List.of(getEntity());
    }

    public static UserEntity getEntity() {
        var entity = new UserEntity();
        entity.username = USERNAME;
        entity.email = EMAIL;
        entity.passwordHash = PASSWORD_HASH;
        entity.role = ROLE;
        entity.active = true;
        return entity;
    }
}
