/**
 * @fileoverview Файл содержит класс контроллера приложения
 * @author Баглай М.В.
 */
package mx.bl;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@SuppressWarnings("unchecked")
public class TasksListController {
    @Autowired
    private TaskValidator taskValidator;

    private List<Task> tsksList;

    TasksListController(){
         tsksList = new ArrayList<Task>();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(HttpSession httpSession) {
        //List<Task> tsksList = new ArrayList<Task>();
        httpSession.setAttribute("tasksList",tsksList);
        return "redirect:/task";
    }

    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public ModelAndView get(HttpSession httpSession){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("TasksList");
        Object tlAttrObj = httpSession.getAttribute("tasksList");
        if(tlAttrObj == null) {
            List<Task> tl = new ArrayList<Task>();
            mav.addObject("tasksList", tl);
            httpSession.setAttribute("tasksList", tl);
        }
        else {
            List<Task> tl = (List<Task>)tlAttrObj;
            mav.addObject("tasksList", tl);
        }
        mav.addObject("task", new Task());

        return mav;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@ModelAttribute("task") Task task, BindingResult result, HttpSession httpSession, Model model) {

        taskValidator.validate(task, result);   // Валидируем на случай отключения js-скриптов
        if (result.hasErrors()){
            List<Task> tl = (List<Task>)httpSession.getAttribute("tasksList");
            model.addAttribute("tasksList", tl);
            return "/TasksList";
        }
        Object tlAttrObj = httpSession.getAttribute("tasksList");
        if(tlAttrObj == null) {                                   // Проверяем случай отсутствия сессии
            List<Task> tl = new ArrayList<Task>();
            model.addAttribute("tasksList", tl);
            model.addAttribute("task", new Task());
            httpSession.setAttribute("tasksList", tl);
            return "/TasksList";
        }

        List<Task> tl = (List<Task>)tlAttrObj;

        Task tsk = null;
        int indx = -1;
        for (Task t:tl){
            if(t.getId().equals(task.getId())) {
                tsk = t;
                indx = tl.indexOf(t);
                break;
            }
        }
        if (indx == -1){
            task.setId(GenID.getInstance().getID());
            tl.add(task);
        }
        else
            tl.set(indx,task);

        return "redirect:/task";
    }

    @RequestMapping(value = "/task/{id}/{action}", method = RequestMethod.GET)
    public String edit(@PathVariable String id, @PathVariable String action, HttpSession httpSession, Model model) {

        Object tlAttrObj = httpSession.getAttribute("tasksList");
        if(tlAttrObj == null) {                                    // Проверяем случай отсутствия сессии
            List<Task> tl = new ArrayList<Task>();
            model.addAttribute("tasksList", tl);
            model.addAttribute("task", new Task());
            httpSession.setAttribute("tasksList", tl);
            return "/TasksList";
        }

        List<Task> tl = (List<Task>)tlAttrObj;
        Task tsk = null;
        int indx = -1;
        for (Task t:tl) {
            if(t.getId() == Long.parseLong(id)) {
                tsk = t;
                indx = tl.indexOf(t);                       // Ищем в списке индекс задачи
                break;
            }
        }

        if (action.equals("delete")) {
            if(indx != -1)                // Исключаем удаление несуществующих объектов
                tl.remove(indx);
            return "redirect:/task";
        }

        if (action.equals("edit") && indx != -1) { // исключаем редактирование несуществующих объектов

                model.addAttribute("task", tsk);
                model.addAttribute("tasksList", tl);
        }
        else {
            model.addAttribute("task", new Task());
            model.addAttribute("tasksList", tl);
            return "redirect:/task";
        }
        return "/TasksList";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

}
