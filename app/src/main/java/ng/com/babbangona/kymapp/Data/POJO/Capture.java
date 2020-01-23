package ng.com.babbangona.kymapp.Data.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Capture {
    @SerializedName("member_id")
    @Expose
    private String member_id;
    @SerializedName("template")
    @Expose
    private String template;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("version")
    @Expose
    private String version;



    //START DATABASE CREATION

    public static final String TABLE_NAME = "captures_records_db";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MEMBER_ID = "member_id";
    public static final String COLUMN_TEMPLATE = "template";
    public static final String COLUMN_TYPE = "type";
    public static final String TIME= "time";
    public static final String SYNC= "synced";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_MEMBER_ID + " TEXT,"
                    + COLUMN_TEMPLATE + " TEXT,"
                    + TIME + " TEXT,"
                    + COLUMN_TYPE + " TEXT,"
                    + SYNC + " TEXT"
                    + ")";


    //END DATABASE CREATION

    //START GETTER & SETTER

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
