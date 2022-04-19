const loginForm = document.getElementById("box");
const loginButton = document.getElementById("submit_login");
const loginErrorMsg  = document.getElementById("");

loginButton.addEventListener("click", (e) =>{
    e.preventDefault();
    const username = loginForm.username.value;
    const password = loginForm.password.value;

    if(username === signUpUsername && password === signUpPassword){
        alert("You have successfully logged in.");
        location.reload();
    } else{
        loginErrorMsg.style.opasity = 1;
    }
}

)

const signUpForm = document.getElementById("sign_up_box");
const signUpButton = document.getElementById("submit_login");

signUpButton.addEventListener("click", (e) =>{
    e.preventDefault();
    const signUpUsername = signUpForm.username.value;
    const signUpPassword = signUpForm.password.value;

})