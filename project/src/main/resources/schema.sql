// Tạo Bảng users: Thông tin người dùng
CREATE TABLE users(
    id_user INT PRIMARY KEY AUTO_INCREMENT,
    last_name varchar(50) NOT NULL,
    first_name varchar(50) NOT NULL,
    email varchar(100) NOT NULL,
    phone_number varchar(10) NOT NULL,
    avatar TEXT,
    role ENUM('USER','ADMIN'),
    status ENUM('BLOCK','ACTIVE','NOT_VERIFIED') DEFAULT 'NOT_VERIFIED'
    )

 //Tạo Bảng books: Thông tin Sách
 CREATE TABLE books(
     id_book INT PRIMARY KEY AUTO_INCREMENT,
     img_book TEXT NOT NULL,
     name_book varchar(100) NOT NULL,
     `language` ENUM('ENGLISH','VIETNAMESE'),
     author varchar(50) NOT NULL,
     category varchar(50) NOT NULL,
     topic varchar(50) NOT NULL,
     price DECIMAL(10,2) NOT NULL,
     quantity int NOT NULL
     )

 // Tạo bảng orders: Đơn hàng
  CREATE TABLE orders(
      order_id INT PRIMARY KEY AUTO_INCREMENT,
      id_user INT NOT NULL,
      total_price DECIMAL(10,2) NOT NULL,
      shipcost_code INT NOT NULL,
      voucher_code INT NOT NULL,
      payment_method ENUM('CASH','TRANSFER')NOT NULL DEFAULT 'CASH',
      status_order ENUM('APPROVING','APPROVED','DELIVERING','DELIVERED', 'CANCELED') NOT NULL,
      handle_order ENUM('ACCEPT','CANCEL') NOT NULL,
      payment DECIMAL(10,2) NOT NULL,
      order_at DATETIME NOT NULL
      )

  // Tạo bảng order_items: Đơn hàng chi tiết
  CREATE TABLE order_items(
      id_order_item INT PRIMARY KEY AUTO_INCREMENT,
      id_order INT NOT NULL,
      id_book INT NOT NULL,
      quantity_buy INT NOT NULL,
      total_price DECIMAL(10,2) NOT NULL
      )


 // Tạo Bảng vouchers: Mã giảm giá
 CREATE TABLE vouchers(
     voucher_code INT PRIMARY KEY AUTO_INCREMENT,
     name_voucher varchar(50) NOT NULL,
     type ENUM('TRANSPORT','DISCOUNT') NOT NULL,
     `value` INT NOT NULL,
     start_time DATE NOT NULL,
     end_time DATE NOT NULL,
     total_quantity INT NOT NULL,
     curent_quantity INT NOT NULL
     )

 // Tạo bảng ship_costs: Chi phí vận chuyển
 CREATE TABLE ship_costs(
     shipcost_code INT PRIMARY KEY AUTO_INCREMENT,
     name_city varchar(50) NOT NULL,
     cost INT NOT NULL
     )
 // Tạo Bảng carts: Giỏ hàng
 CREATE TABLE carts(
     id_cart INT PRIMARY KEY AUTO_INCREMENT,
     id_user INT NOT NULL,
     )

 // Tạo bảng login_histories: Lịch sử đăng nhập
 CREATE TABLE login_histories(
     id_history INT PRIMARY KEY AUTO_INCREMENT,
     id_user INT NOT NULL,
     login_time DATETIME NOT NULL
     )

 // Tạo bảng verify_emails: Xác thực Email
 CREATE TABLE verify_emails(
     id_veri_email INT PRIMARY KEY AUTO_INCREMENT,
     id_user INT NOT NULL,
     verified_email BOOLEAN NOT NULL,
     verification_token_email varchar(255),
     token_lifetime DATETIME
     )

 // Tạo bảng reset_passwords: Lấy lại mật khẩu
 CREATE TABLE reset_passwords(
     id_reset_password INT PRIMARY KEY AUTO_INCREMENT,
     id_user INT NOT NULL,
     reset_password_token varchar(255),
     reset_token_expiry DATETIME
     )

 // Tạo bảng reviews: Đánh giá người dùng
 CREATE TABLE reviews(
     id_review INT PRIMARY KEY AUTO_INCREMENT,
     id_user INT NOT NULL,
     id_book INT NOT NULL,
     star_rate INT,
     `comment` varchar(255),
     review_at DATETIME NOT NULL
     )

  //Tạo bảng cart_items: Chi tiết trong giỏ hàng
  CREATE TABLE cart_items(
      id_cart_item int PRIMARY KEY AUTO_INCREMENT,
      id_book int NOT NULL,
      id_cart int NOT NULL,
      quantity int NOT NULL,
      is_selected BOOLEAN NOT NULL DEFAULT FALSE
      )

 // Nhập dữ liệu bảng ship_costs cố định
 INSERT INTO  ship_costs(name_city,cost)
 VALUES
 ('Hà Nội', 24000),
 ('Hải Phòng', 30000),
 ('Nghệ An', 10000),
 ('Huế', 27000),
 ('Hồ Chí Minh', 60000),
 ('Nha Trang', 20000),
 ('Thanh Hóa', 15000),
 ('Hà Tĩnh', 14000),
 ('Cao Bằng', 45000),
 ('Lào Cai', 35000),
 ('Phú Thọ', 22000),
 ('Đà Nẵng', 31000),
 ('Bắc Ninh', 27000),
 ('Quảng Trị', 23000),
 ('Ninh Bình', 20000),
 ('Khánh Hòa', 50000)

