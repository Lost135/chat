$(function (){
    var loginId = $.cookie("loginId");
    var loginPw= $.cookie("loginPw");
    $.get("friend/showFriend",{loginId:loginId},function (data){
        var lis1 = "";
        var lis2 = "";
        alert(data)
        for (var i = 0; i < data.length; i++) {
            if(data[i].status == "Y" && data[i].userId == loginId){
                var route1 = data[i];
                var li1 = '<tr id="'+route1.friendId+'">\n' +
                    '<td class="td_left" id="">\n' +
                    '<a href="">'+route1.friendName+'</a>\n' +
                    '</td>\n' +
                    '<td class="td_right">\n' +
                    '<input type="button" id="" class="submit_fre" value="删除"/>\n' +
                    '</td>\n' +
                    '</tr>';
                lis1 += li1;
            }else if(data[i].status == "Y" && data[i].friendId == loginId) {
                var route1 = data[i];
                var li1 = '<tr id="' + route1.userId + '">\n' +
                    '<td class="td_left" id="">\n' +
                    '<a href="">' + route1.friendName + '</a>\n' +
                    '</td>\n' +
                    '<td class="td_right">\n' +
                    '<input type="button" id="" class="submit_fre" value="删除"/>\n' +
                    '</td>\n' +
                    '</tr>';
                lis1 += li1;
            }else if(data[i].status == "N" && data[i].friendId == loginId){
                var route2 = data[i];
                var li2 = '<tr id="'+route2.friendId+'">\n' +
                    '<td class="td_left" id="">\n' +
                    '<a href="">'+route2.friendName+'</a>\n' +
                    '</td>\n' +
                    '<td class="td_right">\n' +
                    '<input type="button" id="" class="submit_fre" value="确认"/>\n' +
                    '</td>\n' +
                    '</tr>';
                lis2 += li2;
            }
        }
        $("#friends").html(lis1);
        $("#friendReq").html(lis2);
    });
})