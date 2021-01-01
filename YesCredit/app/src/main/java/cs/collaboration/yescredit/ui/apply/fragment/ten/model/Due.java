package cs.collaboration.yescredit.ui.apply.fragment.ten.model;

import androidx.annotation.NonNull;

public class Due {

    private double benefit;
    private double interest;
    private double penalty;
    private double service;
    private double total;
    private double tax;
    private String displayDue;
    private String displayService;
    private String interestUsed;
    private String tag;

    public Due(double benefit, double interest, double penalty, double service, double total,
               double tax, String displayDue, String displayService, String interestUsed, String tag) {
        this.benefit = benefit;
        this.interest = interest;
        this.penalty = penalty;
        this.service = service;
        this.total = total;
        this.tax = tax;
        this.displayDue = displayDue;
        this.displayService = displayService;
        this.interestUsed = interestUsed;
        this.tag = tag;
    }

    public Due() {
    }

    @NonNull
    @Override
    public String toString() {
        return "Due{" +
                "benefit=" + benefit +
                ", interest=" + interest +
                ", penalty=" + penalty +
                ", service=" + service +
                ", total=" + total +
                ", tax=" + tax +
                ", displayDue='" + displayDue + '\'' +
                ", displayService='" + displayService + '\'' +
                ", interestUsed='" + interestUsed + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }

    public double getBenefit() {
        return benefit;
    }

    public void setBenefit(double benefit) {
        this.benefit = benefit;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public double getService() {
        return service;
    }

    public void setService(double service) {
        this.service = service;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public String getDisplayDue() {
        return displayDue;
    }

    public void setDisplayDue(String displayDue) {
        this.displayDue = displayDue;
    }

    public String getDisplayService() {
        return displayService;
    }

    public void setDisplayService(String displayService) {
        this.displayService = displayService;
    }

    public String getInterestUsed() {
        return interestUsed;
    }

    public void setInterestUsed(String interestUsed) {
        this.interestUsed = interestUsed;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
