const mealsAjaxUrl = "profile/meals/";
const filterForm = $('#filter');
let isFiltered = false;

const ctx = {
    ajaxUrl: mealsAjaxUrl,
    update: function () {
        return isFiltered ? filterTable() : updateTable();
    }
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
        isFiltered = true;
        fillTable(data);
    });
}

function clearFilter() {
    filterForm[0].reset();
    isFiltered = false;
    updateTable();
}