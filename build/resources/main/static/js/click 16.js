$(function() {
    $("[path]").click(function() {
        // 이제 여기있는 변수를 param 으로 넣으면 됨
        var path = $(this).attr("path");
        const xhr = new XMLHttpRequest();
        const method = "GET";
        // 왜 지금까지 이렇게 보낼 생각을 못해봤지?
        const url = "http://localhost:8088/directory?path=" + path; // https 로 해서 404 error 났었음 , http 로 하면 ?
        xhr.open(method , url);

//        console.log(path);
//        xhr.setRequestHeader('Content-type', 'application/json'); // 알고보니까 content type 을 json 으로 하지 않아서 , missing body 라고 나온 것 같았음
//        xhr.setRequestBody(path);
        // 서버에 요청을 보냄 , 내 예상이 맞다면 굳이 응답 처리 하지 말고 , xhr 로 요청만 보내보자.
        // 요청 보내는 것 까지 성공했음
        xhr.send();
//        location.href = url;
    })
})