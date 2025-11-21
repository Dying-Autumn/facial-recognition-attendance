package com.example.studentattendanceterminal.network;

import com.example.studentattendanceterminal.models.AttendanceDTO;
import com.example.studentattendanceterminal.models.Student;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StudentService {
    // 验证学生身份（根据学号获取信息）
    @GET("students/number/{studentNumber}")
    Call<Student> getByNumber(@Path("studentNumber") String studentNumber);

    // 上传考勤记录
    @POST("attendance/record")
    Call<ResponseBody> uploadAttendance(@Body AttendanceDTO record);
}
