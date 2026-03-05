package model.test.testcontent.scheduler.action.startvusers;


import model.test.testcontent.scheduler.action.Action;
import model.test.testcontent.scheduler.action.common.VusersAction;

public class StartVusers extends VusersAction {

    @Override
    public void applyTo(Action.ActionBuilder builder) {
        builder.startVusers(this);
    }

}