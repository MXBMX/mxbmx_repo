/**
 * @fileoverview Файл содержит обработчики и функции клиентской части
 * @author Баглай М.В.
 */

$(function(){

    $("#sideLeft").accordion();

    $('#datepicker').datepicker({
        inline: true,
        dateFormat: "dd/mm/yy",
        showAnim: "slideDown"
    });

    $("input:submit").button();

    // Обработчик Submit кнопки - вызывает валидацию полей Date и Name на клиенте
    $('#Save').bind('click', function(){
        var txtVal =  $('#datepicker').val(),
            txtName = $('#name').val(),
            dialogContent = "<p><span class='ui-icon ui-icon-alert' style='float:left; margin:0 7px 20px 0;'></span>Date is not valid!</p>";

        if(!validateDate(txtVal)){
            $("#dialog-confirm").html(dialogContent);
            $("#dialog-confirm").dialog("open");
            return false;
        }

        var res = validateName(txtName);

        if(res != true){
            dialogContent = dialogContent.replace("Date is not valid!",res);
            $("#dialog-confirm").html(dialogContent);
            $("#dialog-confirm").dialog("open");
            return false;
        }

        return true;
    });

    function validateDate(txtDate)    {
        var currVal = txtDate;
        if(currVal == '')
            return false;

        var rxDatePattern = /^(0?[1-9]|[12][0-9]|3[01])[\/\-](0?[1-9]|1[012])[\/\-]\d{4}$/;
        var dtArray = currVal.match(rxDatePattern); // Проверяем формат

        if (dtArray == null)
            return false;

        //Проверим значения dd/mm/yyyy
        dtMonth = dtArray[3];
        dtDay= dtArray[1];
        dtYear = dtArray[5];

        if (dtMonth < 1 || dtMonth > 12)
            return false;
        else if (dtDay < 1 || dtDay> 31)
            return false;
        else if ((dtMonth==4 || dtMonth==6 || dtMonth==9 || dtMonth==11) && dtDay ==31)
            return false;
        else if (dtMonth == 2)
        {
            var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0));
            if (dtDay> 29 || (dtDay ==29 && !isleap))
                return false;
        }
        return true;
    }

    $("#dialog:ui-dialog").dialog("destroy");

    $("#dialog-confirm").dialog({
        autoOpen: false,
        resizable: false,
        height:140,
        modal: true,
        buttons: {
            "OK": function() {
                $(this).dialog( "close" );
            }
        }
    });

    // Валидация имени задачи - валидирует на предмет длины слова.
    // Опираемся на то, что в русском языке максимальная длинна слова 55 символов (wiki). В английском меньше
    function validateName(val){
        var maxLength = 55,
            empty = 'Name is empty!',
            longw = 'Words in Name is too long!',
            i = 0, j=0, k = 0;

        if (val == '')
            return empty;

        for(;i!=-1;){
            i = val.indexOf(' ',k);
            if(j==0){
                if(i==-1){
                    if(val.length > maxLength)
                        return longw;
                }
                else
                    if(i > maxLength)
                        return longw;
            }
            else {
                if((i - k) > maxLength){
                    return longw;
                }
                else
                    if((i == -1)&&(val.length-j > maxLength))
                        return longw;
            }
            j=i;k=i+1;
        }
    return true;
    }

    function getH4Content(){
        var hmas = document.getElementsByTagName('h4'),
            at = [];
        for(var i=0;i<hmas.length;i++)
            at.push(String.trim(hmas.item(i).innerHTML));
        return at;
    }

    $('#autocomplite').bind('focus', function(){
        var t = getH4Content();
        if(t){
            $( "#autocomplite" ).autocomplete({
                source: t
            });
        }
    });

    var pattern; // Паттерн для подсвечивания задач

    $("#hghlght").button().click(function(){
        pattern = $('#autocomplite').val();
        if(pattern)
            $("h4:contains(" + pattern + ")").css("background-color","gold");
    });

    $("#clr").button().click(function(){
        if(pattern){
            $("h4:contains(" + pattern + ")").css("background-color","transparent");
            $('#autocomplite').val('');
        }
    });

});
