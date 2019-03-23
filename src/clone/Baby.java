package clone;

import java.io.Serializable;

public class Baby implements Cloneable, Serializable {   // 实现 Cloneable 接口（运用序列化实现深拷贝要实现 Serializable）
    private String sex;

    public Baby(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public Baby clone() {   // 重写 clone 方法，将 protected 改为 public，Object 改为 Baby
        Baby baby = null;
        try {
            baby = (Baby) super.clone();    // 调用 super.clone(); 因为 String 对象不可变，浅拷贝也没关系。
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return baby;
    }
}
