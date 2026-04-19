package models;

import java.io.Serializable;
import java.util.Date;
import models.memento.MemberMemento;

public class Member implements Serializable {

    public enum MembershipType {
        STUDENT("Student"),
        TEACHER("Teacher"),
        PUBLIC("Public"),
        SENIOR("Senior Citizen");

        private final String displayName;

        MembershipType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }


    private String memberId;
    private String name;
    private String email;
    private String phone;
    private MembershipType membershipType;
    private Date joinDate;
    private String address;


    public Member() {
    }

    public Member(String memberId, String name, String email, String phone,
                  MembershipType membershipType, Date joinDate, String address) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.membershipType = membershipType;
        this.joinDate = joinDate;
        this.address = address;
    }

 
    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public String getAddress() {
        return address;
    }


    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MemberMemento save() {
        return new MemberMemento(
                memberId,
                name,
                email,
                phone,
                membershipType,
                joinDate,
                address
        );
    }

    public void restore(MemberMemento memento) {
        this.memberId = memento.getMemberId();
        this.name = memento.getName();
        this.email = memento.getEmail();
        this.phone = memento.getPhone();
        this.membershipType = memento.getMembershipType();
        this.joinDate = memento.getJoinDate();
        this.address = memento.getAddress();
    }
}
