# 서버 시작 시 자동으로 가입되는 회원 정보
   #관리자 username: admin, password: admin
   #사용자 username: test1, password: test1
#

INSERT INTO USERS(user_name, password, email, phone, role, user_state) VALUES ('admin','$2a$10$wmJtJThNlK0T1RPLz8dbNOb5aVawrg31XblyMDeNlfZOT/b9nvTzW',' ',' ','ADMIN','active');
INSERT INTO USERS(user_name, password, email, phone, role, user_state) VALUES ('test1','$2a$10$6goYsfitvLY9w3tHUHVx5OUiZ4h6Zxb4EYNdOknlvl6sijOlSU3pi','test1@test.com','01012345678','USER','active');
