package model.test.testcontent.scheduler.action.stopvusers;


import model.test.testcontent.scheduler.action.Action;
import model.test.testcontent.scheduler.action.common.VusersAction;

 public class StopVusers extends VusersAction {

    @Override
    public void applyTo(Action.ActionBuilder builder) {
        builder.stopVusers(this);
    }

}