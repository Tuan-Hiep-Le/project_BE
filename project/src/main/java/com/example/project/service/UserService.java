package com.example.project.service;

import com.example.project.entity.User;

public interface UserService {
    //Thêm user
    public User addUser(User user);

    //Kiểm tra xem Email đã tồn tại hay chưa
    public boolean isExist(String email);

    //Kiểm tra xem tài khoản, mật khẩu đăng nhập đã đúng hay chưa
    public boolean isLoginSuccess(String email, String password);
    //Kiểm tra xem số điện thoại có hợp lệ hay không
    public boolean numberPhoneValid(String numberPhone);

    //Tìm kiếm người dùng bằng email
    public User getUserByEmail(String email);
}
