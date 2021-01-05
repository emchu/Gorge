function register() {
    const email = document.getElementById("register_email").value;
    const password = document.getElementById("pwd_register").value;
    const confirmPassword = document.getElementById("pwd_register2").value;
    const url = 'http://localhost:8080/users/signup';
    if (email != null && password != null && confirmPassword != null) {
        $.ajax({
            type: 'POST',
            url: url,
            dataType: 'html',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({
                email: email,
                password: password,
                confirmPassword: confirmPassword
            }),
            success: function (data, textStatus, xhr) {
                console.log(data.responseText)
                location.href = "http://localhost:8080/api/auth/";
                // alert(data.responseText);
            },
            error: function (data, textStatus, xhr) {
                var alerts = document.getElementsByName('alert-register');

                for (var i = 0; i < alerts.length; i++) {
                    if (!alerts[i].classList.contains("d-none")) {
                        alerts[i].classList.add("d-none")
                    }
                }

                if (data.responseText.includes("1")) {
                    document.getElementById("alert-register-1").classList.remove("d-none");
                } else if (data.responseText.includes("2")) {
                    document.getElementById("alert-register-2").classList.remove("d-none");
                } else if (data.responseText.includes("3")) {
                    document.getElementById("alert-register-3").classList.remove("d-none");
                } else {
                    alert("Błąd");
                }
            }
        });
    }
}