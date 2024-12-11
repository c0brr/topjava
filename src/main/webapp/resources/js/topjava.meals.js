const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$.ajaxSetup({
    converters: {
        "text json": function (result) {
            let json = $.parseJSON(result);
            if (Array.isArray(json)) {
                json.forEach(element => {
                    if (element.hasOwnProperty("dateTime")) {
                        element.dateTime = element.dateTime.substring(0, 16).replace("T", " ");
                    }
                });
            } else {
                if (json.hasOwnProperty("dateTime")) {
                    json.dateTime = json.dateTime.substring(0, 16).replace("T", " ");
                }
            }
            return json;
        }
    }
});

$('#startDate').datetimepicker({
    format: 'Y-m-d',
    timepicker: false,
    onChangeDateTime: function (current_date) {
        $('#endDate').datetimepicker("setOptions", {"minDate": current_date === null ? false : current_date});
    }
})

$('#endDate').datetimepicker({
    format: 'Y-m-d',
    timepicker: false
})

$('#startTime').datetimepicker({
    format: 'H:i',
    datepicker: false,
    onChangeDateTime: function (current_time) {
        $('#endTime').datetimepicker("setOptions", {"minTime": current_time === null ? false : current_time});
    }
})

$('#endTime').datetimepicker({
    format: 'H:i',
    datepicker: false
})

$('#dateTime').datetimepicker({
    format: 'Y-m-d H:i',
    lazyInit: true
})

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
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
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-meal-excess", data.excess);
            }
        })
    );
});