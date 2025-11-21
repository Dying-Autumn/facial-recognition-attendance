package com.example.studentattendanceterminal.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentattendanceterminal.R;
import com.example.studentattendanceterminal.db.AttendanceDbHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.material.button.MaterialButton;
import com.example.studentattendanceterminal.ui.MeFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private RecyclerView rvRecords;
    private MaterialButton btnStartCheckin;
    private TextView tvLocation;
    private ImageView ivPreview;
    private Spinner spinnerCourse;
    private RecordsAdapter adapter;
    private static final int REQ_CODE_CHECKIN = 1001;
    private static final String[] REQUIRED_PERMISSIONS = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private FusedLocationProviderClient fusedClient;
    private double currentLat = 0.0;
    private double currentLon = 0.0;
    private boolean hasLocation = false;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private Context appContext;
    private static final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvRecords = view.findViewById(R.id.rv_records);
        btnStartCheckin = view.findViewById(R.id.btn_start_checkin);
        tvLocation = view.findViewById(R.id.tv_location);
        ivPreview = view.findViewById(R.id.iv_preview);
        spinnerCourse = view.findViewById(R.id.spinner_course);

        rvRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecordsAdapter(new java.util.ArrayList<>());
        rvRecords.setAdapter(adapter);

        fusedClient = LocationServices.getFusedLocationProviderClient(requireContext());
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onCameraResult);

        // 加载课程到 Spinner
        loadCoursesToSpinner();
        // 进入页面时加载现有签到记录
        reloadRecords();

        btnStartCheckin.setOnClickListener(v -> {
            // 先判断是否已登录（存在 student_id）
            Long sid = getLoggedInStudentId();
            if (sid == null) {
                showToast("请先登录学号后再签到");
                // 跳转到“我”页进行登录
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new MeFragment())
                        .commit();
                return;
            }

            if (hasAllPermissions()) {
                proceedCheckin();
            } else {
                requestPermissions(REQUIRED_PERMISSIONS, REQ_CODE_CHECKIN);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 使用应用级 Context，避免 Fragment 生命周期导致的空指针
        appContext = context.getApplicationContext();
    }

    private boolean hasAllPermissions() {
        return hasCameraPermission() && hasLocationPermission();
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void proceedCheckin() {
        showToast("权限已授予，开始采集定位与照片");
        fetchLocationThenCapture();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE_CHECKIN) {
            // 重新评估权限状态（考虑用户选择“近似位置”场景）
            boolean granted = hasCameraPermission() && hasLocationPermission();
            if (granted) {
                proceedCheckin();
            } else {
                showToast("需要相机与定位权限以完成签到");
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void fetchLocationThenCapture() {
        if (!hasLocationPermission()) {
            showToast("定位权限未授予");
            return;
        }

        // 检查设备定位是否开启
        LocationManager lm = (LocationManager) requireContext().getSystemService(android.content.Context.LOCATION_SERVICE);
        boolean enabled = lm != null && lm.isLocationEnabled();
        if (!enabled) {
            showToast("请开启设备定位服务");
            try {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            } catch (Exception ignored) {}
            return;
        }

        // 检查 Google Play 服务可用性（部分设备/模拟器可能缺失）
        int playSvc = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireContext());
        if (playSvc != ConnectionResult.SUCCESS) {
            // 回退到系统 LocationManager 的最近位置
            Location fallback = null;
            try {
                fallback = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (fallback == null) fallback = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } catch (SecurityException ignored) {}
            if (fallback != null) applyLocation(fallback);
            openCamera();
            return;
        }

        int priority = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ? Priority.PRIORITY_HIGH_ACCURACY
                : Priority.PRIORITY_BALANCED_POWER_ACCURACY;

        CancellationTokenSource cts = new CancellationTokenSource();
        fusedClient.getCurrentLocation(priority, cts.getToken())
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        applyLocation(location);
                    } else {
                        // 兜底：尝试拿到上次定位
                        fusedClient.getLastLocation().addOnSuccessListener(loc -> {
                            if (loc != null) applyLocation(loc);
                            openCamera();
                        }).addOnFailureListener(e -> {
                            showToast("无法获取定位：" + e.getMessage());
                            // 再次兜底：尝试 LocationManager 最近位置
                            Location lmLast = null;
                            try {
                                lmLast = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (lmLast == null) lmLast = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            } catch (SecurityException ignored2) {}
                            if (lmLast != null) applyLocation(lmLast);
                            openCamera();
                        });
                        return;
                    }
                    openCamera();
                })
                .addOnFailureListener(e -> {
                    showToast("定位失败：" + e.getMessage());
                    // 兜底：LocationManager 最近位置
                    Location lmLast = null;
                    try {
                        lmLast = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (lmLast == null) lmLast = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    } catch (SecurityException ignored3) {}
                    if (lmLast != null) applyLocation(lmLast);
                    openCamera();
                });
    }

    private void applyLocation(@NonNull Location location) {
        hasLocation = true;
        currentLat = location.getLatitude();
        currentLon = location.getLongitude();
        if (tvLocation != null) {
            tvLocation.setText("定位：" + currentLat + ", " + currentLon);
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }

    private void onCameraResult(ActivityResult result) {
        if (result.getResultCode() != Activity.RESULT_OK) {
            showToast("拍照取消");
            return;
        }
        Intent data = result.getData();
        if (data == null || data.getExtras() == null) {
            showToast("未获取到照片数据");
            return;
        }
        Object extra = data.getExtras().get("data");
        if (!(extra instanceof Bitmap)) {
            showToast("照片数据格式不支持");
            return;
        }
        Bitmap bitmap = (Bitmap) extra;
        if (ivPreview != null) {
            ivPreview.setImageBitmap(bitmap);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
        byte[] imageBytes = bos.toByteArray();
        
        // 将图片转为 Base64 以便上传
        String base64Image = android.util.Base64.encodeToString(imageBytes, android.util.Base64.NO_WRAP);

        long ts = System.currentTimeMillis();
        double lat = currentLat;
        double lon = currentLon;
        boolean success = isWithinChinaBounds(lat, lon);

        // 读取选中的课程与已登录学生
        AttendanceDbHelper.Course selectedCourse = (AttendanceDbHelper.Course) spinnerCourse.getSelectedItem();
        Long courseId = selectedCourse != null ? selectedCourse.id : null;
        Long studentId = getLoggedInStudentId();

        new Thread(() -> {
            Context ctx = appContext != null ? appContext : requireContext();
            long rowId = AttendanceDbHelper.getInstance(ctx)
                    .insertCheckinWithCourse(ts, lat, lon, imageBytes, courseId, studentId, success);
            requireActivity().runOnUiThread(() -> {
                if (rowId > 0) {
                    showToast(success ? "本地保存成功，正在上传..." : "签到失败：位置不在范围内");
                    reloadRecords();
                    
                    // 如果本地保存成功且签到判定为有效，尝试上传服务器
                    if (success) {
                        uploadAttendanceRecord(studentId, courseId, lat, lon, base64Image, ts);
                    }
                } else {
                    showToast("保存失败");
                }
            });
        }).start();
    }
    
    private void uploadAttendanceRecord(Long studentId, Long courseId, double lat, double lon, String base64Image, long ts) {
        com.example.studentattendanceterminal.models.AttendanceDTO record = 
            new com.example.studentattendanceterminal.models.AttendanceDTO(studentId, courseId, lat, lon, base64Image, ts, "正常");
            
        com.example.studentattendanceterminal.network.ApiClient.getStudentService()
            .uploadAttendance(record)
            .enqueue(new retrofit2.Callback<okhttp3.ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<okhttp3.ResponseBody> call, retrofit2.Response<okhttp3.ResponseBody> response) {
                    if (response.isSuccessful()) {
                        showToast("✅ 同步到服务器成功");
                    } else {
                        showToast("❌ 上传失败: " + response.code());
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<okhttp3.ResponseBody> call, Throwable t) {
                    showToast("❌ 网络错误，数据已保存本地");
                }
            });
    }

    private void loadCoursesToSpinner() {
        Context ctx = appContext != null ? appContext : requireContext();
        AttendanceDbHelper db = AttendanceDbHelper.getInstance(ctx);
        java.util.List<AttendanceDbHelper.Course> courses = db.getAllCourses();
        android.widget.ArrayAdapter<AttendanceDbHelper.Course> adapter = new android.widget.ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourse.setAdapter(adapter);
    }

    private void reloadRecords() {
        Context ctx = appContext != null ? appContext : requireContext();
        AttendanceDbHelper db = AttendanceDbHelper.getInstance(ctx);
        java.util.List<AttendanceDbHelper.CheckinDisplay> list = db.getCheckinDisplayList();
        java.util.List<RecordItem> items = new java.util.ArrayList<>();
        for (AttendanceDbHelper.CheckinDisplay d : list) {
            String timeStr = formatTime(d.timestamp);
            items.add(new RecordItem(d.courseName == null ? "未选择课程" : d.courseName, d.success, timeStr));
        }
        adapter.setData(items);
    }

    private Long getLoggedInStudentId() {
        android.content.SharedPreferences prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        long id = prefs.getLong("student_id", -1L);
        return id > 0 ? id : null;
    }

    private boolean isWithinChinaBounds(double lat, double lon) {
        // 东经 73°33′-135°05′，北纬 3°51′-53°33′
        double minLon = 73 + 33.0/60.0;      // 73.55
        double maxLon = 135 + 5.0/60.0;      // 135.083333...
        double minLat = 3 + 51.0/60.0;       // 3.85
        double maxLat = 53 + 33.0/60.0;      // 53.55
        return (lon >= minLon && lon <= maxLon && lat >= minLat && lat <= maxLat);
    }

    private String formatTime(long ts) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new java.util.Date(ts));
    }

    private void showToast(@NonNull String msg) {
        try {
            Context ctx = appContext != null ? appContext : getContext();
            if (ctx != null) {
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
            } else {
                Log.w(TAG, "Toast context is null, msg=" + msg);
            }
        } catch (Exception e) {
            Log.e(TAG, "Toast error: " + e.getMessage());
        }
    }

    private List<RecordItem> sampleData() {
        List<RecordItem> list = new ArrayList<>();
        list.add(new RecordItem("高等数学", true, "2025-11-02 08:30"));
        list.add(new RecordItem("大学英语", false, "2025-11-01 09:20"));
        list.add(new RecordItem("计算机基础", true, "2025-10-30 14:05"));
        return list;
    }

    // 简易模型
    static class RecordItem {
        final String courseName;
        final boolean success;
        final String time;

        RecordItem(String courseName, boolean success, String time) {
            this.courseName = courseName;
            this.success = success;
            this.time = time;
        }
    }

    // 简易适配器
    static class RecordsAdapter extends RecyclerView.Adapter<RecordsViewHolder> {
        private List<RecordItem> data;

        RecordsAdapter(List<RecordItem> data) { this.data = data; }

        @NonNull
        @Override
        public RecordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
            return new RecordsViewHolder(item);
        }

        @Override
        public void onBindViewHolder(@NonNull RecordsViewHolder holder, int position) {
            RecordItem item = data.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() { return data == null ? 0 : data.size(); }

        void setData(List<RecordItem> newData) {
            this.data = newData;
            notifyDataSetChanged();
        }
    }

    static class RecordsViewHolder extends RecyclerView.ViewHolder {
        private final android.widget.TextView tvCourse;
        private final android.widget.TextView tvStatus;
        private final android.widget.TextView tvTime;

        RecordsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourse = itemView.findViewById(R.id.tv_course);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTime = itemView.findViewById(R.id.tv_time);
        }

        void bind(@NonNull RecordItem item) {
            tvCourse.setText(item.courseName);
            tvStatus.setText(item.success ? "成功" : "失败");
            tvTime.setText(item.time);
        }
    }
}
