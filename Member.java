
package models;

import java.io.Serializable;
import java.util.Date;

public class Member implements Serializable {
    
    
    public enum MembershipType 
    {
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
    
    // Member attributes
    private String memberId;
    private String name;
    private String email;
    private String phone;
    private MembershipType membershipType;
    private Date joinDate;
    private String address;
    
    // Default constructor
    public Member() {
    }
    
    // Parameterized constructor
    public Member(String memberId, String name, String email, String phone,MembershipType membershipType, Date joinDate, String address) 
    {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.membershipType = membershipType;
        this.joinDate = joinDate;
        this.address = address;
    }
    
    // Getters
    public String getMemberId() 
    { 
        return memberId; 
    }
    public String getName() 
    { 
        return name; 
    }
    public String getEmail() 
    { 
        return email; 
    }
    public String getPhone() 
    { 
        return phone; 
    }
    public MembershipType getMembershipType() 
    { 
        return membershipType; 
    }
    public Date getJoinDate() 
    { 
        return joinDate; 
    }
    public String getAddress() 
    { 
        return address; 
    }
    
    // Setters
    public void setMemberId(String memberId) 
    { 
        this.memberId = memberId; 
    }
    public void setName(String name) 
    { 
        this.name = name; 
    }
    public void setEmail(String email) 
    { 
        this.email = email; 
    }
    public void setPhone(String phone) 
    { 
        this.phone = phone; 
    }
    public void setMembershipType(MembershipType membershipType) 
    { 
        this.membershipType = membershipType; 
    }
    public void setJoinDate(Date joinDate) 
    { 
        this.joinDate = joinDate; 
    }
    public void setAddress(String address) 
    { 
        this.address = address; 
    }
}

