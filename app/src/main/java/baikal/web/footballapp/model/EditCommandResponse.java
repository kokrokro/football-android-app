package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EditCommandResponse {
    //    @SerializedName("editCommand")
//    @Expose
//    private EditCommand editCommand;
//    public EditCommand getCommnd() {
//        return editCommand;
//    }
//
//    public void setCommand(EditCommand editCommand) {
//        this.editCommand = editCommand;
//    }
//}
    @SerializedName("players")
    @Expose
    private List<Player> players = null;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

}
