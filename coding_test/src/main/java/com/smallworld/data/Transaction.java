package com.smallworld.data;

public class Transaction {
    private Integer mtn;
    private double amount;
    private String senderFullName;
    private Integer senderAge;
    private String beneficiaryFullName;
    private Integer beneficiaryAge;
    private Integer issueId;
    private boolean issueSolved;
    private String issueMessage;
    public Transaction() {
    }
    public Transaction(Integer mtn, double amount, String senderFullName, Integer senderAge, String beneficiaryFullName,
            Integer beneficiaryAge, Integer issueId, boolean issueSolved, String issueMessage) {
        this.mtn = mtn;
        this.amount = amount;
        this.senderFullName = senderFullName;
        this.senderAge = senderAge;
        this.beneficiaryFullName = beneficiaryFullName;
        this.beneficiaryAge = beneficiaryAge;
        this.issueId = issueId;
        this.issueSolved = issueSolved;
        this.issueMessage = issueMessage;
    }
    public Integer getMtn() {
        return mtn;
    }
    public void setMtn(Integer mtn) {
        this.mtn = mtn;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getSenderFullName() {
        return senderFullName;
    }
    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }
    public Integer getSenderAge() {
        return senderAge;
    }
    public void setSenderAge(Integer senderAge) {
        this.senderAge = senderAge;
    }
    public String getBeneficiaryFullName() {
        return beneficiaryFullName;
    }
    public void setBeneficiaryFullName(String beneficiaryFullName) {
        this.beneficiaryFullName = beneficiaryFullName;
    }
    public Integer getBeneficiaryAge() {
        return beneficiaryAge;
    }
    public void setBeneficiaryAge(Integer beneficiaryAge) {
        this.beneficiaryAge = beneficiaryAge;
    }
    public Integer getIssueId() {
        return issueId;
    }
    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }
    public boolean isIssueSolved() {
        return issueSolved;
    }
    public void setIssueSolved(boolean issueSolved) {
        this.issueSolved = issueSolved;
    }
    public String getIssueMessage() {
        return issueMessage;
    }
    public void setIssueMessage(String issueMessage) {
        this.issueMessage = issueMessage;
    }
    @Override
    public String toString() {
        return "Transaction [mtn=" + mtn + ", amount=" + amount + ", senderFullName=" + senderFullName + ", senderAge="
                + senderAge + ", beneficiaryFullName=" + beneficiaryFullName + ", beneficiaryAge=" + beneficiaryAge
                + ", issueId=" + issueId + ", issueSolved=" + issueSolved + ", issueMessage=" + issueMessage + "]";
    }
}
