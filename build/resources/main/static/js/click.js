$(function() {
    $("[directory-url]").click(function() {
        var url = $(this).attr("directory-url");
        location.href = url;
    })

    $("[file-url]").click(function() {
        var path = $(this).attr("file-url");
        var newWindow = window.open("about:blank");

        newWindow.location.href = path;
    })

    $("[content]").click(function() {
        var content = $(this).attr("content") // content 정보를 얻고
        $("#file_content").text(content);
    })
})

