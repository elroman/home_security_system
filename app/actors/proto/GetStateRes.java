package actors.proto;

public class GetStateRes {

    private Boolean activate;

    public GetStateRes(final Boolean activate) {
        this.activate = activate;
    }

    public Boolean getActivate() {
        return activate;
    }

    @Override
    public String toString() {
        return "GetStateRes{" +
            "activate=" + activate +
            '}';
    }
}
