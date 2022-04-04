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

     // 일단 해당 version 을 클릭하게 되면 , 해당 version 에 대한 정보가 넘어와야 하는데
     // 아무래도 versionDto 에다가 path 도 추가해야 할 것 같다.
     // 그래야지 content 를 누르게 되더라도
//    $("[content]").click(function(){
//        var content = $(this).attr("content")
//        var path = $(this).attr("path") // path , content 둘다 얻어냈음 , 그러면 여기서 어떻게 가면 될까
//        console.log(content)
//        console.log(path)
//        $.ajax({
//            type: "GET",
//            url: "http://localhost:8088/files",
//            dataType: 'json',
//            data: {"path": path, "content": content}
//        })
//        // 여기서 무조건 적으로 content 와 path 를 담아서 보내야한다 .. 어떻게 보낼까?
//    })

    $("[content]").click(function() {
        var content = $(this).attr("content") // content 정보를 얻고
        $("#file_content").text(content);
    })
})

