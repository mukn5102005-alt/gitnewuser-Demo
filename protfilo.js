 function sendMsg() {
    let nameInput = document.getElementById("nameInput");
    let emailInput = document.getElementById("emailInput");
    let msgInput = document.getElementById("msg");

    let name = nameInput.value;
    let email = emailInput.value;
    let msg = msgInput.value;

    if(name === "" || email === "" || msg === "") {
        alert("Please fill all fields!");
    } else {
        alert("Message Sent ✅");

        // fields clear karna
        nameInput.value = "";
        emailInput.value = "";
        msgInput.value = "";
    }
}
sendMsg();