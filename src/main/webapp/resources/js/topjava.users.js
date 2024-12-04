const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    update: function () {
        updateTable();
    }
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function enable(element, id) {
    let isChecked = !!$(element).is(":checked");
    $.ajax({
        type: "PATCH",
        url: userAjaxUrl + id,
        data: JSON.stringify(isChecked),
        contentType: "application/json",
        error: function () {
            $(element).prop('checked', !isChecked);
        },
    }).done(function () {
        $(element).closest("tr")[isChecked ? 'removeClass' : 'addClass']('disabled');
        successNoty(isChecked ? "Enabled" : "Disabled");
    });
}