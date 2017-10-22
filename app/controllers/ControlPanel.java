package controllers;

import com.google.inject.Inject;

import javax.inject.Named;

import actors.cmd.ActivationCmd;
import actors.cmd.TakePhotoCmd;
import akka.actor.ActorRef;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.control_panel_page;

public class ControlPanel
    extends Controller {

    @Inject
    @Named("cameraActor")
    ActorRef cameraActor;

    @Inject
    @Named("securitySystem")
    ActorRef securitySystem;

    public Result controlPanelPage() {
        return ok(control_panel_page.render("Your new application is ready."));
    }

    public Result activateSecurity() {
        securitySystem.tell(new ActivationCmd(true), ActorRef.noSender());

        return ok("activate done!");
    }

    public Result deactivateSecurity() {
        securitySystem.tell(new ActivationCmd(false), ActorRef.noSender());

        return ok("Deactivate done!");
    }

    public Result takePicture() {

        cameraActor.tell(new TakePhotoCmd(), ActorRef.noSender());

        return ok(control_panel_page.render("Your new application is ready."));
    }
}
