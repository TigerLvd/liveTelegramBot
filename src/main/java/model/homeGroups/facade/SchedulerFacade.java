package model.homeGroups.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
public class SchedulerFacade {
    private static Logger log = LoggerFactory.getLogger(SchedulerFacade.class);

    private DBFacade dbFacade;

    private BotFacade botFacade;

    public void checkLostStatInfos() {
        //
    }
}
