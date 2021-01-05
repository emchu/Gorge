function login() {
    console.log("Hello world!");
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const url = 'http://localhost:8080/users/signin';
    console.log(email);
    if (email != null && password!= null) {
        $.ajax({
            type: 'POST',
            url: url,
            contentType : 'application/json',
            data        : JSON.stringify({
                email: email,
                password : password
            }),
            success: function (response){
                const token = response.accessToken
                const auth = response.tokenType
                // document.cookie = auth.concat("=", token)
                document.cookie = auth+"="+token+"; path=/"
                // console.log(getCookie("Bearer"))
                $.ajaxSetup({
                    headers: { 'Bearer' : getCookie("Bearer")}
                });
                location.reload();
            },
            error: function(jqXHR, textStatus, errorThrown){
                document.getElementById("alert_login").classList.remove("d-none");
                // setTimeout(function () {
                //     document.getElementById("alert1").classList.add("d-none")
                // },5000);
            }
        });
    }
}

function logout() {
    const url = 'http://localhost:8080/users/logout';
    $.ajax({
        type: 'POST',
        url: url,
        success : function(data) {
            document.cookie = "Bearer= ; expires = Thu, 01 Jan 1970 00:00:00 GMT"
            location.href = "http://localhost:8080/api/auth/";
        }
    });
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}



