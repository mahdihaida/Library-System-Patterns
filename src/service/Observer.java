package service;

import Model.Media; // لازم نستورد الميديا لأننا نستخدمها هنا

public interface Observer {
    // الدالة التي سيتم استدعاؤها عند إضافة ميديا جديدة
    void update(Media media);

}