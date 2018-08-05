package jspindor.gpstracker.models;

import java.util.List;

public class Group {
    private int groupId;
    private String invitationCode;
    private List<User> users;

    public Group() {
    }

    public Group(int groupId, String invitationCode, List<User> users) {
        this.groupId = groupId;
        this.invitationCode = invitationCode;
        this.users = users;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
