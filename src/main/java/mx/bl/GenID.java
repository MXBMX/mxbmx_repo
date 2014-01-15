/**
 * @fileoverview Файл содержит класс генератора ID для объектов Task
 * @author Баглай М.В.
 */
package mx.bl;

//Singletone
public class GenID {
    private static GenID instance;
    private Long value = 0L;
    private GenID(){
    }
    public static synchronized GenID getInstance(){
        if (instance == null)
            instance = new GenID();
        return instance;
    }
    public Long getID(){
        return this.value++;
    }
}
