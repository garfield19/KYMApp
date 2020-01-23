package ng.com.babbangona.kymapp.Data.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Members {
    @SerializedName("member_name")
    @Expose
    private String memberName;
    @SerializedName("ik_number")
    @Expose
    private String ikNumber;
    @SerializedName("unique_member_id")
    @Expose
    private String uniqueMemberId;
    @SerializedName("member_role")
    @Expose
    private String memberRole;
    @SerializedName("picker")
    @Expose
    private String picker;
    @SerializedName("template")
    @Expose
    private String template;
    @SerializedName("done")
    @Expose
    private String done;
    @SerializedName("card")
    @Expose
    private String card;
    @SerializedName("m_date")
    @Expose
    private String m_date;
    @SerializedName("pass_done")
    @Expose
    private String pass_done;
    @SerializedName("fail_picture_done")
    @Expose
    private String fail_picture_done;
    @SerializedName("approved")
    @Expose
    private String approved;
    @SerializedName("member_id")
    @Expose
    private String member_id;


    public static final String TABLE_NAME = "members_records_db";

    public static final String COLUMN_ID = "id";
    public static final String MEMBER_NAME = "member_name";
    public static final String COLUMN_MEMBER_ID = "member_id";
    public static final String PICKER = "picker";
    public static final String IK_NUMBER = "ik_number";
    public static final String COLUMN_TEMPLATE = "template";
    public static final String DONE= "done";
    public static final String ROLE= "role";
    public static final String CARD= "card";
    public static final String PASS_DONE= "pass_done";
    public static final String PC_APPROVED= "approved";
    public static final String FAIL_PICTURE_DONE= "fail_picture_done";
    public static final String MEMBER_ID= "old_member_id";

//START DATABASE CREATION
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + MEMBER_NAME + " TEXT,"
                    + COLUMN_MEMBER_ID + " TEXT,"
                    + IK_NUMBER + " TEXT,"
                    + ROLE + " TEXT,"
                    + PICKER + " TEXT,"
                    + DONE + " TEXT,"
                    + COLUMN_TEMPLATE + " TEXT,"
                    + PC_APPROVED + " TEXT,"
                    + CARD + " TEXT,"
                    + PASS_DONE + " TEXT,"
                    + FAIL_PICTURE_DONE + " TEXT,"
                    + MEMBER_ID + " TEXT"
                    + ")";

    //END DATABASE CREATION

    //START GETTER & SETTER

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getIkNumber() {
        return ikNumber;
    }

    public void setIkNumber(String ikNumber) {
        this.ikNumber = ikNumber;
    }

    public String getUniqueMemberId() {
        return uniqueMemberId;
    }

    public void setUniqueMemberId(String uniqueMemberId) {
        this.uniqueMemberId = uniqueMemberId;
    }

    public String getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(String memberRole) {
        this.memberRole = memberRole;
    }

    public String getPicker() {
        return picker;
    }

    public void setPicker(String picker) {
        this.picker = picker;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getM_date() {
        return m_date;
    }

    public void setM_date(String m_date) {
        this.m_date = m_date;
    }

    public String getPass_done() {
        return pass_done;
    }

    public void setPass_done(String pass_done) {
        this.pass_done = pass_done;
    }

    public String getFail_picture_done() {
        return fail_picture_done;
    }

    public void setFail_picture_done(String fail_picture_done) {
        this.fail_picture_done = fail_picture_done;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }
}
