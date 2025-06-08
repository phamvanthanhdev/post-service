document.addEventListener("DOMContentLoaded", function() {
    const token = localStorage.getItem("token");
    if (token) {
        // Gửi token trong mọi yêu cầu AJAX
        const xhr = new XMLHttpRequest();
        xhr.open("GET", "/user", true);
        xhr.setRequestHeader("Authorization", "Bearer " + token);
        xhr.send();
    }
});