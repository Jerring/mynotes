package clone;

import java.io.*;

/**
 * 使用克隆方法的三个正确步骤：
 * 1.类要实现 Cloneable 接口（作为可以 clone 的标志，它没有定义任何方法），否则调用 clone 方法时会抛出 CloneNotSupportedException。
 * 2.重写 clone 方法，并将访问权限从 protected 改为 public，否则包外的类访问不到这个方法。
 * 3.可根据需要将 clone 方法的返回值从 Object 改为对应的类型，避免使用时进行强制类型转换。
 * （引用类型要单独调用 clone 方法才能进行深拷贝，不然也是浅拷贝，对应的类也要实现 Cloneable 接口）
 */
public class Person implements Cloneable, Serializable {  // 实现 Cloneable 接口（运用序列化实现深拷贝要实现 Serializable）
    private int age;
    private String name;
    private Baby baby;

    @Override
    public Person clone() { // 重写 clone 方法，将 protected 改为 public，Object 改为 Person
        Person p = null;
        try {
            p = (Person) super.clone(); // 首先调用 super.clone();
            p.baby = baby.clone();      // 单独 clone 以实现深拷贝（若是集合类型，元素也要单独拷贝）
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return p;
    }

    public Person(int age, String name, Baby baby) {
        this.age = age;
        this.name = name;
        this.baby = baby;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        Baby bao = new Baby("男");
        Person person1 = new Person(18, "MIMI", bao);
        Person person2 = null;
//        person2 = person1.clone();
        person2 = CloneUtils.clone(person1);
        System.out.println(person2.getName());
        person1.baby.setSex("女");
        // 深拷贝输出“男”，浅拷贝输出“女”
        System.out.println(person2.baby.getSex());
    }

}