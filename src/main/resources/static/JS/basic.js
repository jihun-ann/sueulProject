$(function(){
    $('.menuSub').mouseover(function (){
        $('.menu-wrapper').css({"display":"flex"});
    })
    $('.menuSub').mouseleave(function (){
        $('.menu-wrapper').css({"display":"none"});
    })

    $('.font').mouseover(function(e){
        if($(e.target).hasClass('koreaMapArea')){
            let val = e.target.getAttribute('value');
            $('#koreaMap').css({"background":"url(/mapImage/"+val+".png)  no-repeat center","background-size":"100%"});
            e.target.style.color = "black";
        }else if ($(e.target).hasClass('listDetail')){
            $('.listDetail').css({"background":"url(/mapImage/listDetailSelect.png)  no-repeat center","background-size":"100% 100%"});
            e.target.style.color = "black";
        }else{
            e.target.style.color = "black";
        }
    })
    $('.font').mouseleave(function(e){
        if($(e.target).hasClass('koreaMapArea')){
            $('#koreaMap').css({"background":"url(/mapImage/koreaMap.png)  no-repeat center","background-size":"100%"});
            e.target.style.color = "#8b8b8b";
        }else if ($(e.target).hasClass('listDetail')){
            $('.listDetail').css({"background":"url(/mapImage/listDetailNonSelect.png)  no-repeat center","background-size":"100% 85%"});
            e.target.style.color = "#8b8b8b";
        }else{
            e.target.style.color = "#8b8b8b"
        }
    })

    $('.slide-imgC').mouseover(function (e){
        $(e.currentTarget).children('img').css({'opacity':'1'});
        $(e.currentTarget).children('span').css({'color':'black'});
    })
    $('.slide-imgC').mouseleave(function (e){
        $(e.currentTarget).children('img').css({'opacity':'0.5'});
        $(e.currentTarget).children('span').css({'color':'#8b8b8b'});
    })

    $('.fa-hexagon').click(function(){
        bigImgCurrentIdx = 0;
        bigListWrapper.style.left = (bigImgCurrentIdx*-320) + 'px';
    })
})