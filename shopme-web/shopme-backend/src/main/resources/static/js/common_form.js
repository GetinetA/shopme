$(document).ready(function () {
    // here the Thymeleaf syntax isn't parsed dynamically ("[[@{/users}]]"), so declare var inside
    // the page and use it here - moduleURL
    $("#btnCancel").on("click", function (){
        window.location = moduleURL;
    })

    $("#fileImage").change(function () {
        fileSize = this.files[0].size;
        //alert("File size: " + fileSize);
        if (fileSize > 1048576) {
            this.setCustomValidity("You must choose an image less than 1MB!");
            this.reportValidity();
        } else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
    });
});

function showImageThumbnail(fileInput) {
    let file = fileInput.files[0];
    let reader = new FileReader();
    reader.onload = function (ev) {
        $("#thumbnail").attr("src", ev.target.result);
    };

    reader.readAsDataURL(file);
}

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();
}
function showWarningModalDialog(message) {
    showModalDialog("Warning", message);
}
function showErrorModalDialog(message) {
    showModalDialog("Error", message);
}