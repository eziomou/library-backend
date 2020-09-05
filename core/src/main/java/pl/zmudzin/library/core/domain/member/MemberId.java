package pl.zmudzin.library.core.domain.member;

import pl.zmudzin.library.core.domain.common.StringId;

public class MemberId extends StringId {

    public MemberId(String value) {
        super(value);
    }

    public static MemberId of(String value) {
        return new MemberId(value);
    }
}
