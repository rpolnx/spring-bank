package xyz.rpolnx.spring_bank.account.model.enums;

public enum AccountStatus {
    ACTIVE,
    CREATING,
    INACTIVE;

    public static AccountStatus[] usableStatus() {
        return new AccountStatus[]{ACTIVE, CREATING};
    }
}
