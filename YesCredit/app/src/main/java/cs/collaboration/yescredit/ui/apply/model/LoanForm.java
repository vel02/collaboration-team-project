package cs.collaboration.yescredit.ui.apply.model;

import androidx.annotation.NonNull;

public class LoanForm {

    private String levelOfEducation;
    private String reason;
    private String moreDetails;
    private String outstanding;
    private String civilStatus;
    private String sourceOfIncome;
    private String incomePerMonth;

    public LoanForm(String levelOfEducation, String reason, String moreDetails, String outstanding,
                    String civilStatus, String sourceOfIncome, String incomePerMonth) {
        this.levelOfEducation = levelOfEducation;
        this.reason = reason;
        this.moreDetails = moreDetails;
        this.outstanding = outstanding;
        this.civilStatus = civilStatus;
        this.sourceOfIncome = sourceOfIncome;
        this.incomePerMonth = incomePerMonth;
    }

    public LoanForm() {
    }

    @NonNull
    @Override
    public String toString() {
        return "LoanForm{" +
                "levelOfEducation='" + levelOfEducation + '\'' +
                ", reason='" + reason + '\'' +
                ", moreDetails='" + moreDetails + '\'' +
                ", outstanding='" + outstanding + '\'' +
                ", civilStatus='" + civilStatus + '\'' +
                ", sourceOfIncome='" + sourceOfIncome + '\'' +
                ", incomePerMonth='" + incomePerMonth + '\'' +
                '}';
    }

    public String getLevelOfEducation() {
        return levelOfEducation;
    }

    public void setLevelOfEducation(String levelOfEducation) {
        this.levelOfEducation = levelOfEducation;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMoreDetails() {
        return moreDetails;
    }

    public void setMoreDetails(String moreDetails) {
        this.moreDetails = moreDetails;
    }

    public String getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(String outstanding) {
        this.outstanding = outstanding;
    }

    public String getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getSourceOfIncome() {
        return sourceOfIncome;
    }

    public void setSourceOfIncome(String sourceOfIncome) {
        this.sourceOfIncome = sourceOfIncome;
    }

    public String getIncomePerMonth() {
        return incomePerMonth;
    }

    public void setIncomePerMonth(String incomePerMonth) {
        this.incomePerMonth = incomePerMonth;
    }
}
