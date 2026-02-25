async function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const responseElement = document.getElementById("badCreds");

    try {
        const response = await fetch("http://localhost:1003/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ username, password })
        });

        const data = await response.json();

        if (response.ok) {
            // Login successful, save JWT in localStorage
            localStorage.setItem("jwtToken", data.token);
            responseElement.textContent = "Login successful!";
            responseElement.style.color = "green";

            // Redirect or call your protected API
            // window.location.href = "/dashboard.html";
        } else {
            // Login failed
            responseElement.textContent = data.message || "Invalid credentials";
            responseElement.style.color = "red";
        }
    } catch (error) {
        console.error(error);
        responseElement.textContent = "Server error, try again later.";
        responseElement.style.color = "red";
    }
}