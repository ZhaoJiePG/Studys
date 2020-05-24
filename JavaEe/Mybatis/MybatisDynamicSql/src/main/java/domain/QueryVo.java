package domain;

import java.util.List;

/**
 * Created by ZJ on 2020/5/17
 * comment:
 */
public class QueryVo {

    private User user;

    public List getIds() {
        return ids;
    }

    public void setIds(List ids) {
        this.ids = ids;
    }

    private List ids;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
