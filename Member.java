package models;
import java.io.Serializable;
import java.util.Date;

import strategy.FineStrategy;
import strategy.FixedFineStrategy; // Strategy folder link korar jonno
import strategy.NoFineStrategy;
import strategy.PerDayFineStrategy;

public class Member implements Serializable {
    
    public enum MembershipType {
        STUDENT("Student"), TEACHER("Teacher"), PUBLIC("Public"), SENIOR("Senior Citizen");
        private final String displayName;
        MembershipType(String displayName) { this.displayName = displayName; }
        @Override public String toString() { return displayName; }
    }
    
    private String memberId, name, email, phone, address;
    private MembershipType membershipType;
    private Date joinDate;
    
    // Strategy field (transient jate save korar somoy error na hoy)
    private transient FineStrategy fineStrategy;

    public Member() { }
    
    public Member(String memberId, String name, String email, String phone, MembershipType membershipType, Date joinDate, String address) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.membershipType = membershipType;
        this.joinDate = joinDate;
        this.address = address;
        setStrategy(); // Object toiri hobar somoy strategy set hobe
    }
    
    // Mul Strategy logic
    private void setStrategy() {
        if (membershipType == null) return;
        switch (membershipType) {
            case TEACHER: this.fineStrategy = new NoFineStrategy(); break;
            case STUDENT: this.fineStrategy = new PerDayFineStrategy(5.0); break;
            case SENIOR: this.fineStrategy = new FixedFineStrategy(20.0); break;
            default: this.fineStrategy = new FixedFineStrategy(50.0); break;
        }
    }

    // GUI theke ei method-ti call hobe
    public double calculateFine(int daysLate) {
        if (this.fineStrategy == null) setStrategy();
        return (fineStrategy != null) ? fineStrategy.calculateFine(daysLate) : 0.0;
    }

    // Getters and Setters (Apnar deya ager gulo thik ache)
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public MembershipType getMembershipType() { return membershipType; }
    public void setMembershipType(MembershipType membershipType) { 
        this.membershipType = membershipType; 
        setStrategy(); // Type change hole strategy update hobe
    }
    public Date getJoinDate() { return joinDate; }
    public void setJoinDate(Date joinDate) { this.joinDate = joinDate; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}