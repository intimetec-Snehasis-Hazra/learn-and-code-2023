package src.main.java.com.cafeteria;

public class VotingResult {
    private int menuItemId;
    private String menuItemName;
    private int voteCount;

    public VotingResult(int menuItemId, String menuItemName, int voteCount) {
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.voteCount = voteCount;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public int getVoteCount() {
        return voteCount;
    }
}
