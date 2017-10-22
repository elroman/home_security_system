package actors.cmd;

public class ActivationCmd {

    private boolean active;

    public ActivationCmd(final boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "ActivationCmd{" +
            "active=" + active +
            '}';
    }
}
