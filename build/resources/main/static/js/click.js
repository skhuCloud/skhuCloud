$(function() {
    $("[directory-url]").click(function() {
        var url = $(this).attr("directory-url");
        location.href = url;
    })

    $("[file-url]").click(function() {
        // 여기서 새로운 창을 띄우고 거기다가 해당 지금 file-url 을 보내야함

        // 방법을 찾았음 , 새로운 창을 띄우는데 완전히 새로운 창이 아닌 , goo.gl 처럼 열려면 , window 객체를 이용해서 새로운 창을 띄우고
        // 거기다가 location.href 를 통해서 현재 요청한 url을 집어넣으면 됨
        var newWindow = window.open("about:blank");
        newWindow.location.href = $(this).attr("file-url");
    })
})

