 package service;

import Model.Media;

public interface Subject {
    void attach(Observer o);       // إضافة مراقب (طالب)
    void detach(Observer o);       // حذف مراقب
    void notifyObservers(Media m); // تنبيه الجميع
}
