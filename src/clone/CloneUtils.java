package clone;

import java.io.*;

/**
 * 实现序列化实现深拷贝，只需要对应的类实现 Serializable 接口（它的属性里面的类也要实现）。
 */
public class CloneUtils {
    public static <T extends Serializable> T clone(T obj) {
        T cloneObj = null;
        ByteArrayOutputStream out = null;
        ObjectOutputStream oos = null;
        ByteArrayInputStream in = null;
        ObjectInputStream ois = null;
        try {
            out = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(out);
            oos.writeObject(obj);
            in = new ByteArrayInputStream(out.toByteArray());
            ois = new ObjectInputStream(in);
            cloneObj = (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {}
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {}
            }
        }
        return cloneObj;
    }
}