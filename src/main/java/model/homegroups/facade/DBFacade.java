package model.homegroups.facade;

import model.homegroups.service.HomeGroupService;
import model.homegroups.service.StatInfoService;
import model.homegroups.service.UserService;

public class DBFacade {

    private UserService userService;

    private HomeGroupService homeGroupService;

    private StatInfoService statInfoService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public HomeGroupService getHomeGroupService() {
        return homeGroupService;
    }

    public void setHomeGroupService(HomeGroupService homeGroupService) {
        this.homeGroupService = homeGroupService;
    }

    public StatInfoService getStatInfoService() {
        return statInfoService;
    }

    public void setStatInfoService(StatInfoService statInfoService) {
        this.statInfoService = statInfoService;
    }
}
