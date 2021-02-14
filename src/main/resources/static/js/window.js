$(function (){
    //根据textarea 标签内容自适应高度
    $.each($("textarea"), function(i, n){
        $(n).css("height", n.scrollHeight + "px");
    })
    var ele = $(".rg_form_center");
    ele.scrollTop(ele.prop('scrollHeight'));

})

