/**
 * @fileoverview Файл содержит класс задачи
 * @author Баглай М.В.
 */

package mx.bl;

public class Task implements Comparable {
    private Long id;
    private String date;
    private String name;
    private Boolean status;

    public Task() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int compareTo(Object obj) {       // Оставляем возможность сравнения объектов класса, на случай сортировки в коллекциях
        if(!(obj instanceof Task)){
            throw new ClassCastException("Invalid object");
        }
        Task t = (Task)obj;
        if (this.id > t.getId())
            return 1;
        else
            if (this.id.equals(t.getId()))
                return 0;
        else return -1;

    }
}
