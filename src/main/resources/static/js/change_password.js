function changePassword() {
    console.log("Hello world!");
    const password = document.getElementById("pwd").value;
    const new_password = document.getElementById("new_pwd").value;
    const new_password_2 = document.getElementById("new_pwd2").value;
    const url = 'http://localhost:8080/password/change';
    if (new_password != null && new_password_2 != null && password != null) {
        console.log(getCookie("Bearer"));
        $.ajax({
            type: 'PUT',
            url: url,
            dataType: 'html',
            contentType: 'application/json; charset=utf-8',
            headers: {
                'Bearer': getCookie("Bearer")
            },
            data: JSON.stringify({
                password: password,
                newPassword: new_password,
                newPassword2: new_password_2
            }),
            success: function (data, textStatus, xhr) {
                console.log(data.responseText)
                location.href = "http://localhost:8080/api/auth/";
                // alert(data.responseText);
            },
            error: function (data, textStatus, xhr) {
                console.log(data.responseText)
                var alerts = document.getElementsByName('alert_change');

                for (var i = 0; i < alerts.length; i++) {
                    if (!alerts[i].classList.contains("d-none")) {
                        alerts[i].classList.add("d-none")
                    }
                }

                if (data.responseText.includes("1")) {
                    document.getElementById("alert1").classList.remove("d-none");
                } else if (data.responseText.includes("2")) {
                    document.getElementById("alert2").classList.remove("d-none");
                } else if (data.responseText.includes("3")){
                    document.getElementById("alert3").classList.remove("d-none");
                } else {
                    alert("Błąd");
                }
            }
        });
    }
}