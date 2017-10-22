package controllers;

import static akka.pattern.Patterns.ask;

import com.google.inject.Inject;

import javax.inject.Named;

import actors.cmd.ActivationCmd;
import actors.cmd.TakePhotoCmd;
import actors.proto.GetStateReq;
import actors.proto.GetStateRes;
import akka.actor.ActorRef;
import akka.dispatch.Mapper;
import play.Logger;
import play.libs.Akka;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.Future;
import views.html.control_panel_page;

public class ControlPanel
    extends Controller {

    @Inject
    @Named("cameraActor")
    ActorRef cameraActor;

    @Inject
    @Named("securitySystem")
    ActorRef securitySystem;

    public F.Promise<Result> controlPanelPage() {
        return F.Promise.wrap(getSecuritySystemState())
            .map(resp -> (Result) ok(control_panel_page.render("Your new application is ready.", resp)))
            .recover(t -> {
                Logger.error("Problem connection to controlPanelPage:" + t.toString());
                return (Result) ok(control_panel_page.render("Your new application is ready.", false));
            });
    }

    public F.Promise<Result> activateSecurity() {
        securitySystem.tell(new ActivationCmd(true), ActorRef.noSender());

        return F.Promise.wrap(getSecuritySystemState())
            .map(resp -> (Result) ok(control_panel_page.render("Your new application is ready.", resp)))
            .recover(t -> {
                Logger.error("Problem connection to controlPanelPage:" + t.toString());
                return (Result) ok(control_panel_page.render("Your new application is ready.", false));
            });
    }

    public F.Promise<Result> deactivateSecurity() {
        securitySystem.tell(new ActivationCmd(false), ActorRef.noSender());

        return F.Promise.wrap(getSecuritySystemState())
            .map(resp -> (Result) ok(control_panel_page.render("Your new application is ready.", resp)))
            .recover(t -> {
                Logger.error("Problem connection to controlPanelPage:" + t.toString());
                return (Result) ok(control_panel_page.render("Your new application is ready.", false));
            });
    }

    public F.Promise<Result> takePicture() {
        cameraActor.tell(new TakePhotoCmd(), ActorRef.noSender());

        return F.Promise.wrap(getSecuritySystemState())
            .map(resp -> (Result) ok(control_panel_page.render("Your new application is ready.", resp)))
            .recover(t -> {
                Logger.error("Problem connection to controlPanelPage:" + t.toString());
                return (Result) ok(control_panel_page.render("Your new application is ready.", false));
            });
    }

    private Future<Boolean> getSecuritySystemState() {
        return ask(securitySystem, new GetStateReq(), 10_000).map(new Mapper<Object, Boolean>() {
            @Override
            public Boolean apply(Object parameter) {
                boolean active = false;
                if (parameter instanceof GetStateRes) {
                    active = ((GetStateRes) parameter).getActivate();
                }
                return active;
            }
        }, Akka.system().dispatcher());
    }

}
