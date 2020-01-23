package ng.com.babbangona.kymapp.Data.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Verification {
    @SerializedName("member_id")
    @Expose
    private String member_id;
    @SerializedName("pass_flag")
    @Expose
    private String pass_flag;
    @SerializedName("tries")
    @Expose
    private String tries;
    @SerializedName("ik_number")
    @Expose
    private String ik_number;
    @SerializedName("picture_taken")
    @Expose
    private String picture_taken;
    @SerializedName("t_time")
    @Expose
    private String t_time;
    @SerializedName("bg_card")
    @Expose
    private String bg_card;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("card")
    @Expose
    private String card;
    @SerializedName("wrong_card")
    @Expose
    private String wrong_card;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("old_collection_type")
    @Expose
    private String old_collection_type;
    @SerializedName("account_update")
    @Expose
    private String account_update;


//START DATABASE CREATION
    public static final String TABLE_NAME = "verification_records_db";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MEMBER_ID = "member_id";
    public static final String PASS_FLAG = "pass_flag";
    public static final String TRIES = "tries";
    public static final String PICTURE_TAKEN = "picture_taken";
    public static final String TIME= "t_time";
    public static final String IK_NUMBER =  "ik_number";
    public static final String BG_CARD =  "bg_card";
    public static final String SYNC =  "synced";
    public static final String CARD= "card";
    public static final String WRONG_CARD= "wrong_card";
    public static final String ACCOUNT_UPDATE= "account_update";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_MEMBER_ID + " TEXT,"
                    + IK_NUMBER + " TEXT,"
                    + PASS_FLAG + " TEXT,"
                    + TRIES + " TEXT,"
                    + PICTURE_TAKEN + " TEXT,"
                    + BG_CARD + " TEXT,"
                    + SYNC + " TEXT,"
                    + TIME + " TEXT,"
                    + CARD + " TEXT,"
                    + WRONG_CARD + " TEXT,"
                    + ACCOUNT_UPDATE + " TEXT"
                    + ")";

    //END DATABASE CREATION

    //START GETTER & SETTER

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getPass_flag() {
        return pass_flag;
    }

    public void setPass_flag(String pass_flag) {
        this.pass_flag = pass_flag;
    }

    public String getTries() {
        return tries;
    }

    public void setTries(String tries) {
        this.tries = tries;
    }

    public String getIk_number() {
        return ik_number;
    }

    public void setIk_number(String ik_number) {
        this.ik_number = ik_number;
    }

    public String getPicture_taken() {
        return picture_taken;
    }

    public void setPicture_taken(String picture_taken) {
        this.picture_taken = picture_taken;
    }

    public String getTime() {
        return t_time;
    }

    public void setTime(String t_time) {
        this.t_time = t_time;
    }

    public String getBg_card() {
        return bg_card;
    }

    public void setBg_card(String bg_card) {
        this.bg_card = bg_card;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getT_time() {
        return t_time;
    }

    public void setT_time(String t_time) {
        this.t_time = t_time;
    }

    public String getWrong_card() {
        return wrong_card;
    }

    public void setWrong_card(String wrong_card) {
        this.wrong_card = wrong_card;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOld_collection_type() {
        return old_collection_type;
    }

    public void setOld_collection_type(String old_collection_type) {
        this.old_collection_type = old_collection_type;
    }

    public String getAccount_update() {
        return account_update;
    }

    public void setAccount_update(String account_update) {
        this.account_update = account_update;
    }
}
