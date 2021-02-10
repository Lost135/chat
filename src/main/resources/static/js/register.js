$(function () {
   $("#registerForm").submit(function (){
           $.post("user/register", $(this).serialize(), function (data){
                   $("#errorMsg").html(data.errorMsg);
           });
       return false;
   });

});

