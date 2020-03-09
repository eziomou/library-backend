package pl.zmudzin.library.application.member;

import pl.zmudzin.library.application.account.AccountBasicData;

/**
 * @author Piotr Żmudzin
 */
public class MemberBasicData extends AccountBasicData {

    public MemberBasicData() {
        super();
    }

    public MemberBasicData(String username, String firstName, String lastName) {
        super(username, firstName, lastName);
    }
}
