package model.homegroups.facade;

import model.homegroups.chain.Chain;
import model.homegroups.db.User;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@EnableScheduling
public class SchedulerFacade {
    private BotFacade botFacade;

    private DBFacade dbFacade;

    public void checkLostStatInfos() {
        List<User> allUsers = dbFacade.getUserService().findAll();
        for (User user : allUsers) {
            if (null == user.getTelegramUserId() || !user.isNotificationEnabled()) {
                continue;
            }

            botFacade.sendLostStatInfos(dbFacade, user.getTelegramUserId(), user, Chain.buildKeyboardForUser(user), false);
        }
    }

    public BotFacade getBotFacade() {
        return botFacade;
    }

    public void setBotFacade(BotFacade botFacade) {
        this.botFacade = botFacade;
    }

    public DBFacade getDbFacade() {
        return dbFacade;
    }

    public void setDbFacade(DBFacade dbFacade) {
        this.dbFacade = dbFacade;
    }
}
