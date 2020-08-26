package common;


import org.springframework.beans.factory.annotation.Value;

public class Constants {

    public static final Boolean STATUS_BOOKING_DEFAULT = false;

    public static final Boolean STATE_BOOKING_DEFAULT = true;
    /* Longin */

    public static final String LOGIN_FAILE = "Tai khoan hoac mat khau khong chinh xac";

    /* Type Utensil*/

    public static final String UTENSIL_BOOKING_TYPE = "TBLD";

    public static final String UTENSIL_ROOM_TYPE = "TBCD";

    /* Validation field */

    public static final String USERNAME_NULL = "Tai khoan khong duoc de trong";

    public static final String CODE_NULL = "Ma khong the de trong";

    public static final String PASSWORD_NULL = "Mat khau khong duoc de trong";

    public static final String NEW_PASSWORD_NULL = "Mat khau moi khong duoc de trong";

    public static final String PASSWORD_CONFIRM_NULL = "Mat khau nhap lai khong the de trong";

    public static final String NEW_PASSWORD_CONFIRM_NULL = "Mat khau moi nhap lai khong the de trong";

    public static final String FIRST_NAME_NULL = "Ho khong duoc de trong";

    public static final String LAST_NAME_NULL = "Ten khong duoc de trong";

    public static final String EMAIL_NULL = "Email dang duoc de trong";

    public static final String PHONE_NULL = "So dien thoai khong duoc de trong";

    public static final String MAXIMUM_BOOKING_DATE = "Thoi gian dat phong toi da khong duoc de trong";

    public static final String EMAIL_PATTERN = "Email khong dung dinh dang";

    /* Status */

    public static final String CREATED_OK = "Them moi thanh cong";

    public static final String UPDATED_OK = "Cập nhật thành công!";

    public static final String CREATED_FAILE = "Da ton tai, khong the luu ban ghi";

    public static final String UPDATED_FAILE = "Da co loi xay ra, he thong khong de luu ban ghi";

    public static final String SELECT_FAILE = "Da co loi xay ra, khong lay duoc du lieu";

    public static final String SERVER_ERROR = "Đã có lỗi xáy ra từ hệ thống";

    /* ThaoLC */
    public static final String TIME_BOOKING_FAIL = "Vượt quá thời gian đặt phòng, đặt phòng họp thất bại";
    public static final String TIME_BOOKING_SHORT_FIFTEEN_MITUES = "Mỗi phòng họp cần đặt cách nhau 15 phút, đặt phòng họp thất bại";
    public static final String TOTIME_THAN_FROMTIME = "Thời gian họp Đến giờ phải lớn hơn Từ giờ";
    public static final String ERROR_CODE1 = "Thời gian đặt lịch trùng";
    public static final String ERROR_CODE2 = "Thời gian đặt lịch không quá 15p so với bản ghi có sẵn";
    public static final String ERROR_CODE3 = "abcdef";
    public static final String ERROR_CODE4 = "abcdef";
    public static final String ERROR_CODE5 = "abcdef";
    public static final String SUCCESS_CODE = "Đặt phòng họp thành công";
    public static final String RESET_PASSWORD_FAIL = "Reset mật khẩu thất bại, có lỗi hệ thống xảy ra";
    public static final String RESET_PASSWORD_OK = "Reset mật khẩu thành công";
    /*TanNV*/

    public static final String EMAIL_IS_EXIST = "email đã tồn tại";

    public static final String CREATE_HR_OK = "Them nhan su thanh cong!";
    public static final String HR_CODE_EXIST = "Ma nhan su da ton tai";
    public static final String CREATE_HR_FALSE = "Them moi nhan su that bai";

    public static final String UPDATED_ROOM_OK = "Cap nhap thanh cong!";
    public static final String UPDATED_ROOM_FAILE = "Da co loi xay ra, khong the luu ban ghi";

    public static final String DELETE_HR_OK = "Khóa nhân sự thành công!";
    public static final String UNLOCK_HR_OK = "Mở khóa nhân sự thành công!";
    public static final String DELETE_HR_FALI = "Khóa nhân sự thất bại, có rằng buộc dữ liệu";

    /* --- Status --- */

    public static final String STATUS_ACTIVE = "ACTIVE";

    public static final String STATUS_INACTIVE = "INACTIVE";

    public static final String STATUS_NULL = "Trạng thái đang để trống";

    /* CODE Bộ phận-vai trò */
    public static final String BOD = "BOD";
    public static final String BA_MANAGER = "BA Manager";
    public static final String QA_MANAGER = "QA Manager";
    public static final String PM_TEAMLEAD = "PM/TeamLead";
    public static final String TESTLEAD = "TestLead";
    public static final String DEV = "Dev";
    public static final String ADMIN = "ADMIN";
    /* MSG GROUP PERMISSION */
    public static final String ACTIVE_GROUP_PERMISSION = "ACTIVE";
    public static final String INACTIVE_GROUP_PERMISSION = "INACTIVE";
    public static final int DELETE_GROUP_PERMISSION = 0;
    public static final String DEPARTMENTS_EMPTY = "Phong ban đang trống";
    public static final String PERMISSIONS_EMPTY = "Danh sách quyền đang trống";
    public static final String NAME_NULL = "Tên nhóm quyền đang để trống";
    public static final String SAVE_GROUP_PERMISSION_ERROR = "Lỗi hệ thống, nhóm quyền chưa được lưu";
    public static final String SAVE_DEPARTMENTS_ERROR = "Lỗi hệ thống, danh sách phòng ban thuộc nhóm quyền chưa được lưu";
    public static final String SAVE_PERMISSIONS_ERROR = "Lỗi hệ thống, danh sách quyền chưa được lưu";
    public static final String SAVE_USER_ERROR = "Lỗi hệ thống, user chưa được lưu";
    public static final String DELETE_DEPARMENTS_ERROR = "Lỗi hệ thống, xóa danh sách phòng ban không thành công";
    public static final String DELETE_PERMISSIONS_ERROR = "Lỗi hệ thống, xóa danh sách quyền không thành công";
    public static final String OBJ_NOT_FOUND = "Bản ghi không tồn tại";
    public static final String USER_NOT_FOUND = "Bản ghi với thông tin user không tồn tại";
    public static final String OBJ_EXIST = "Bản ghi đã tồn tại";
    public static final String PERMISSION_LIST_EXIST = "Nhóm quyền đã tồn tại, không thể thêm";
    public static final String GROUP_PERMISSION_INACTIVE = "Nhóm quyền không còn hoạt động không thể xóa";
    //hungnv
    public static final String OLD_PASSWORD_FAILE = "Mật khẩu hiện tại không đúng";
    public static final Integer IS_NOT_NEW = 0;
    public static final String EMAIL_NOT_FOUND = "Email không tồn tại";
    public static final String VERIFY_KEY_NOT_FOUND = "Key xác nhận không đúng";

//    //server
////    public static final String FOLDER_SAVE_FILE = "/home/iist/services/qlda-service-phase1/fileUpload/";
//    //local
//
//    @Value("${folderUpload}")
//    public String FOLDER_SAVE_FILE;
}
