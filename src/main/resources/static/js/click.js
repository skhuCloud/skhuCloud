$(function() {
    $("[directory-url]").click(function() {
        var url = $(this).attr("directory-url");
        location.href = url;
    })

    $("[file-url]").click(function() {
        // 여기서 새로운 창을 띄우고 거기다가 해당 지금 file-url 을 보내야함
        window.open($(this).attr("file-url") , '_blank' , 'height=' + screen.height + ',width=' + screen.width + 'fullscreen=yes');
    })
})