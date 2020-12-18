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
    private String limit;
    private String status;
    private String repayment_interest;
    private String repayment_interest_used;
    private String repayment_tax;
    private String repayment_total;
    private String repayment_penalty;
    private String repayment_date;
    private String repayment_loan;
    private String repayment_days;


    public LoanForm(String levelOfEducation, String reason, String moreDetails, String outstanding,
                    String civilStatus, String sourceOfIncome, String incomePerMonth, String limit,
                    String status, String repayment_interest, String repayment_interest_used, String repayment_tax, String repayment_total,
                    String repayment_penalty, String repayment_date, String repayment_loan,
                    String repayment_days) {
        this.levelOfEducation = levelOfEducation;
        this.reason = reason;
        this.moreDetails = moreDetails;
        this.outstanding = outstanding;
        this.civilStatus = civilStatus;
        this.sourceOfIncome = sourceOfIncome;
        this.incomePerMonth = incomePerMonth;
        this.limit = limit;
        this.status = status;
        this.repayment_interest = repayment_interest;
        this.repayment_interest_used = repayment_interest_used;
        this.repayment_tax = repayment_tax;
        this.repayment_total = repayment_total;
        this.repayment_penalty = repayment_penalty;
        this.repayment_date = repayment_date;
        this.repayment_loan = repayment_loan;
        this.repayment_days = repayment_days;
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
                ", limit='" + limit + '\'' +
                ", status='" + status + '\'' +
                ", repayment_interest='" + repayment_interest + '\'' +
                ", repayment_interest_used='" + repayment_interest_used + '\'' +
                ", repayment_tax='" + repayment_tax + '\'' +
                ", repayment_total='" + repayment_total + '\'' +
                ", repayment_penalty='" + repayment_penalty + '\'' +
                ", repayment_date='" + repayment_date + '\'' +
                ", repayment_loan='" + repayment_loan + '\'' +
                ", repayment_days='" + repayment_days + '\'' +
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

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRepayment_interest() {
        return repayment_interest;
    }

    public void setRepayment_interest(String repayment_interest) {
        this.repayment_interest = repayment_interest;
    }

    public String getRepayment_interest_used() {
        return repayment_interest_used;
    }

    public void setRepayment_interest_used(String repayment_interest_used) {
        this.repayment_interest_used = repayment_interest_used;
    }

    public String getRepayment_tax() {
        return repayment_tax;
    }

    public void setRepayment_tax(String repayment_tax) {
        this.repayment_tax = repayment_tax;
    }

    public String getRepayment_total() {
        return repayment_total;
    }

    public void setRepayment_total(String repayment_total) {
        this.repayment_total = repayment_total;
    }

    public String getRepayment_penalty() {
        return repayment_penalty;
    }

    public void setRepayment_penalty(String repayment_penalty) {
        this.repayment_penalty = repayment_penalty;
    }

    public String getRepayment_date() {
        return repayment_date;
    }

    public void setRepayment_date(String repayment_date) {
        this.repayment_date = repayment_date;
    }

    public String getRepayment_loan() {
        return repayment_loan;
    }

    public void setRepayment_loan(String repayment_loan) {
        this.repayment_loan = repayment_loan;
    }

    public String getRepayment_days() {
        return repayment_days;
    }

    public void setRepayment_days(String repayment_days) {
        this.repayment_days = repayment_days;
    }
}
