const mealsAjaxUrl = "profile/meals/";
const filterForm = $('#filter');

const ctx = {
    ajaxUrl: mealsAjaxUrl
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
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
                    "desc"
                ]
            ]
        })
    );
});

function filterTable() {
    $.ajax({
        url: ctx.ajaxUrl + "filter",
        type: "GET",
        data: filterForm.serialize()
    }).done(function (data) {
        fillTable(data);
        isFiltered = true;
    });
}

function clearFilter() {
    filterForm[0].reset();
    updateTable();
}