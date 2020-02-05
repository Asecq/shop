package com.alii.shope;

public class accountingadapter {

    private String Cridet , Debit,ItemAccEntry_ID,AccEntry_ID , Account_ID;

    public accountingadapter() {
    }

    public accountingadapter(String cridet, String debit, String itemAccEntry_ID, String accEntry_ID , String Account_ID ){
        Cridet = cridet;
        Debit = debit;
        ItemAccEntry_ID = itemAccEntry_ID;
        AccEntry_ID = accEntry_ID;
        Account_ID =Account_ID;

    }

    public String getAccount_ID() {
        return Account_ID;
    }

    public void setAccount_ID(String account_ID) {
        Account_ID = account_ID;
    }

    public String getCridet() {
        return Cridet;
    }

    public void setCridet(String cridet) {
        Cridet = cridet;
    }

    public String getDebit() {
        return Debit;
    }

    public void setDebit(String debit) {
        Debit = debit;
    }

    public String getItemAccEntry_ID() {
        return ItemAccEntry_ID;
    }

    public void setItemAccEntry_ID(String itemAccEntry_ID) {
        ItemAccEntry_ID = itemAccEntry_ID;
    }

    public String getAccEntry_ID() {
        return AccEntry_ID;
    }

    public void setAccEntry_ID(String accEntry_ID) {
        AccEntry_ID = accEntry_ID;
    }
}
