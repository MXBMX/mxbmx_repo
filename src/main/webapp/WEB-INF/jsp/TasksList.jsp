<%@ page contentType="text/html; charset=UTF-8"  language="java" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
    <title>Simple task list editor</title>
    <link rel="stylesheet" href="<c:url value="/resources/css/taskslist.css" />" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="<c:url value="/resources/plugins/JQueryUI/css/blitzer/jquery-ui-1.8.22.custom.css" />" type="text/css" media="screen, projection">
    <script src="<c:url value="/resources/plugins/JQueryUI/js/jquery-1.7.2.min.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/plugins/JQueryUI/js/jquery-ui-1.8.22.custom.min.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/TasksList.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/plugins/contextMenu.js"/>" type="text/javascript"></script>
    <script type="text/javascript">
        function InitDragAndDrop(id){
            $( "#droppable" ).droppable({
                drop: function( event, ui ){
                    $("#draggable" + id.toString()).hide();
                    location.replace(window.location.pathname +"/" + id + "/delete");
                }
            });

            $( "#draggable" + id.toString() ).draggable();
        }
        function InitContextMenu(){
            $("tr[context='yes']").contextMenu('taskMenu', {
                bindings: {
                    'contextEdit': function(t) {
                        var editId = t.id.replace('tr','');
                        location.replace(window.location.pathname +"/" + editId + "/edit");
                    },
                    'contextDelete': function(t) {
                        var deleteId = t.id.replace('tr','');
                        location.replace(window.location.pathname +"/" + deleteId + "/delete");
                    }
                }
            });
        }
    </script>
</head>
<body>
<div id="wrapper">
    <div id="header">
    </div>
    <div id="middle">
        <div id="container">
            <div id="content">
                <h3>Tasks</h3>
                <br/>
                <table id="tasks" onmouseover="InitContextMenu()">
                    <tbody>

                        <c:choose>
                            <c:when test="${fn:length(tasksList) == 0}">
                                <tr><td align="center" width="500px">No tasks</td></tr>
                            </c:when>
                            <c:when test="${fn:length(tasksList) != 0}">
                                <div align="right" style="float: right;margin-left:5px;">
                                    <img id="droppable" src="<c:url value="/resources/css/recycle-empty.gif"/>" alt="Trash">
                                </div>
                                <p align="right">You can drop in trash task icon to delete this task</p>
                                <c:forEach items="${tasksList}" var="t">
                                    <tr id="tr${t.id}" context='yes'>
                                        <td width="500px">
                                            <p class="task-date">
                                                <c:out value="${t.date}"/>
                                            </p>
                                            <div style="float: left;margin-right:5px;">
                                                <img id="draggable${t.id}" onmouseover="InitDragAndDrop(${t.id})" src="<c:url value="/resources/css/task.png"/>" alt="Task">
                                            </div>
                                            <h4>
                                                <c:out value="${t.name}"/>
                                            </h4>
                                            <br/>
                                            <c:choose>
                                                <c:when test="${t.status == true}"><p style="color: blue;">Done</p></c:when>
                                                <c:when test="${t.status == false}"><p style="color: red;">To do</p></c:when>
                                            </c:choose>
                                            <p>
                                                <a href='<c:url value="/task/${t.id}/edit"/>'>
                                                    <img src="<c:url value="/resources/css/arrow.gif"/>" alt="Edit">
                                                    Edit
                                                </a>
                                                <a href='<c:url value="/task/${t.id}/delete"/>'>
                                                    <img src="<c:url value="/resources/css/arrow.gif"/>" alt="Delete">
                                                    Delete
                                                </a>
                                            </p>
                                            <hr />
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                    </tbody>
                </table>
            </div>
    </div>
        <div  class="sidebar" id="sideLeft">
            <h3><a href="#">Add/Edit task</a></h3>
            <div>
                <form:form modelAttribute="task">
                    <form:hidden path="id"/>
                    <table>
                        <tr>
                            <td>Date</td>
                            <td><form:input path="date" id="datepicker" autocomplete="off"/><form:errors path="date" cssStyle="color: red"/></td>
                        </tr>
                        <tr>
                            <td>Name</td>
                            <td><form:input path="name" id="name" autocomplete="off"/><form:errors path="name" cssStyle="color: red"/></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><form:checkbox path="status" label="Done"/> </td>
                        </tr>
                        <tr></tr>
                        <tr>
                            <td></td>
                            <td align="right"><input type="submit" value="Save" id="Save"></td>
                        </tr>
                    </table>
                </form:form>
            </div>
            <h3><a href="#">Highlight task</a></h3>
            <div>
                <p align="justify">
                    Enter task name which you want to highlight and then click "Highlight" button.
                    All tasks that contain this text will be highlighted.
                    To clear highlighted tasks click "Clear" button.
                </p>
                <div align="right">
                    <input id="autocomplite">
                    <br/><br/>
                    <input type="button" value="Highlight" id="hghlght">
                    <input type="button" value="Clear" id="clr">
                </div>
            </div>
        </div>
    </div>
    <div id="footer">
        <div class="footer">
            <div class="copyright">
                Copyright&copy; 2012 MaX<br />
                Developed:
                <a href="mailto:max_by@mail.ru">Baglay M.V.</a>
            </div>
        </div>
    </div>
    <div id="dialog-confirm" title="Validation" class="alert-dialog">
    </div>
    <div class="contextMenu" id="taskMenu">
        <ul>
            <li id="contextEdit"><img src="<c:url value="/resources/css/edit.gif"/>" alt="Edit"/> Edit</li>
            <li id="contextDelete"><img src="<c:url value="/resources/css/del.png"/>" alt="Delete"/> Delete</li>
        </ul>
    </div>
</div>
</body>
</html>
