package cs.collaboration.yescredit.model;

import androidx.annotation.NonNull;

public class Code {

    /*
        user: all codes he created (fromId) (code_status)
        friend: a code equal to code from user (code) (referred_status)
     */

    private String codeId;
    private String fromId;
    private String referred_status;//to friend
    private String code_status;//to user
    private String code_benefit;
    private String code;

    public Code(String codeId, String fromId, String referred_status, String code_status,
                String code_benefit, String code) {
        this.codeId = codeId;
        this.fromId = fromId;
        this.referred_status = referred_status;
        this.code_status = code_status;
        this.code_benefit = code_benefit;
        this.code = code;
    }

    public Code() {
    }

    @NonNull
    @Override
    public String toString() {
        return "Code{" +
                "codeId='" + codeId + '\'' +
                ", fromId='" + fromId + '\'' +
                ", referred_status='" + referred_status + '\'' +
                ", code_status='" + code_status + '\'' +
                ", code_benefit='" + code_benefit + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getReferred_status() {
        return referred_status;
    }

    public void setReferred_status(String referred_status) {
        this.referred_status = referred_status;
    }

    public String getCode_status() {
        return code_status;
    }

    public void setCode_status(String code_status) {
        this.code_status = code_status;
    }

    public String getCode_benefit() {
        return code_benefit;
    }

    public void setCode_benefit(String code_benefit) {
        this.code_benefit = code_benefit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
