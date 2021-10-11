
$(document).ready(function () {

    $(".link-delete").on("click", function (e){
        e.preventDefault();
        //alert($(this).attr("href"));
        let link = $(this);
        let entityId = link.attr("entityId");
        let entityName = link.attr("entityName");
        $("#yesDeleteBtn").attr("href", link.attr("href"));
        $("#deleteDialogText").text("Are you sure you want to delete this " + entityName + " ID " + entityId + "?");
        $("#deleteDialog").modal();
    });
});

function clearFilter() {
    // here the Thymeleaf syntax isn't parsed dynamically ("[[@{/users}]]"), so declare var inside
    // the page and use it here - moduleListURL
    window.location = moduleListURL;
}