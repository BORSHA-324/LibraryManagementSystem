package models.memento;

import java.util.Date;
import models.Member.MembershipType;

public class MemberMemento {

    private final String memberId;
    private final String name;
    private final String email;
    private final String phone;
    private final MembershipType membershipType;
    private final Date joinDate;
    private final String address;

    public MemberMemento(String memberId, String name, String email,
                         String phone, MembershipType membershipType,
                         Date joinDate, String address) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.membershipType = membershipType;
        this.joinDate = joinDate;
        this.address = address;
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public MembershipType getMembershipType() { return membershipType; }
    public Date getJoinDate() { return joinDate; }
    public String getAddress() { return address; }
}

