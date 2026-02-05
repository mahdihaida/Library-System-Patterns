package service;

import Model.Admin;
import Model.Student;
import Model.User;
import java.util.List;

public class AuthService {

    public static User login(String username, String password) {
        // 1. التحقق من الأدمن (بيانات ثابتة حسب المطلوب حالياً)
        if (username.equals("admin") && password.equals("admin123")) {
            return new Admin("admin", "admin123");
        }

        // 2. التحقق من الطلاب (من القائمة المحملة في النظام عبر XML)
        // نحصل على قائمة المراقبين المسجلين في النظام
        List<Observer> observers = LibrarySystem.getInstance().getObservers();

        for (Observer o : observers) {
            // نتأكد أن المراقب هو طالب
            if (o instanceof Student) {
                Student s = (Student) o;
                // التحقق من الاسم والباسورد
                if (s.getUsername().equals(username) && s.getPassword().equals(password)) {
                    return s; // تم العثور على الطالب
                }
            }
        }

        return null; // فشل تسجيل الدخول
    }
}